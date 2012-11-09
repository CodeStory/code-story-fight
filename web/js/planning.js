var dayLabels = {
  '2012-11-12':'Monday, Nov. 12 2012',
  '2012-11-13':'Tuesday, Nov. 13 2012',
  '2012-11-14':'Wednesday, Nov. 14 2012',
  '2012-11-15':'Thursday, Nov. 15 2012',
  '2012-11-16':'Friday, Nov. 16 2012'
};

var dayNames = {
  '2012-11-12':'Monday',
  '2012-11-13':'Tuesday',
  '2012-11-14':'Wednesday',
  '2012-11-15':'Thursday',
  '2012-11-16':'Friday'
};

function set_content(selector, template, data) {
  $(selector).html(Hogan.compile($(template).html()).render(data));
}

function initAuthenticationState() {
  var authenticated = ($.cookie('userId') != null);
  var headerTemplateParameters = {
    'authenticated':authenticated
  };
  if (authenticated) {
    headerTemplateParameters.screenName = $.cookie('screenName');
  }
  set_content('#auth', '#header-template', headerTemplateParameters);
}

function refreshStars() {
  var authenticated = ($.cookie('userId') != null);
  if (!authenticated) {
    return $.when();
  }

  return $.getJSON('stars', function (json) {
    $('.star').removeClass('starred').html('');

    _.each(json, function (talkId) {
      $('#talk-' + talkId + ' .star').addClass('starred').html('starred');
    });
  });
}

function enrichPlanning(planning) {
  _.each(planning.days, function (day) {
    day.dayLabel = dayLabels[day.day];
    day.dayName = dayNames[day.day];

    _.each(day.slots, function (slot) {
      _.each(slot.talks, function (talk) {
        talk.speakers_string = talk.speakers.join(', ');
      });
    });
  });
}

function refreshPlanning() {
  return $.getJSON('planning.json', function (json) {
    enrichPlanning(json);
    set_content('#content', '#talks-template', json);
    refreshStars().done(filterTalks);
  });
}

function listenStarClicks() {
  $(document).on('click', '.star', function () {
    var talkId = $(this).attr('data-talk');

    if ($(this).hasClass('starred')) {
      $.post('unstar', { talkId:talkId }, refreshStars).error(redirectToAuthentication);
    } else {
      $.post('star', { talkId:talkId }, refreshStars).error(redirectToAuthentication);
    }

    return false;
  });
}

function redirectToAuthentication() {
  window.location = "/user/authenticate";
}

var filter = undefined;

function filterTalks() {
  var text = $('#search_box').val();
  if (text == filter) {
    return;
  }
  filter = text;
  console.log('Filtering on "' + filter + '" on ' + new Date());

  $('.day_wrapper, .toc-link, .slot, .talk').show();

  if (text.length < 3) {
    return;
  }

  var words = _.filter(text.toLowerCase().split(' '), function (word) {
    return word.length > 0;
  });

  $('.talk').filter(function () {
    var fulltext = $(this).text().toLowerCase();
    return !_.all(words, function (word) {
      return fulltext.indexOf(word) > 0;
    });
  }).hide();

  $('.slot:not(:has(.talk:visible))').hide();
  $('.day_wrapper:not(:has(.slot:visible))').hide();
  $('.hour:hidden').each(function () {
    $('.toc-link[href="#' + this.id + '"]').hide();
  });
}

function supports_history_api() {
  return !!(window.history && history.pushState);
}

function supports_html5_storage() {
  try {
    return 'localStorage' in window && window['localStorage'] !== null;
  } catch (e) {
    return false;
  }
}

function updateUrlWithSearchQuery() {
  if (!supports_history_api()) {
    return;
  }

  var searchQuery = $('#search_box').val();
  var url = $.url();
  var newUrl = url.attr('protocol') + '://' + url.attr('host');
  if (url.attr('port') != '80') {
    newUrl += ':' + url.attr('port');
  }
  newUrl += url.attr('path');
  if (searchQuery != '') {
    newUrl += '?q=' + searchQuery;
  }

  if (url.attr('source') != newUrl) {
    history.pushState(null, null, newUrl);
  }
}

function listenSearch() {
  var searchTimer = null;

  $(document).on('keyup', '#search_box', function () {
    if (searchTimer == null) {
      searchTimer = window.setTimeout(function () {
        searchTimer = null;
        updateUrlWithSearchQuery();
        filterTalks();
      }, 700);
    }
  });
}

function setFilterFromQueryString() {
  $('#search_box').val($.url().param('q'));
}

function listenBackButton() {
  window.addEventListener("popstate", function () {
    setFilterFromQueryString();
    filterTalks();
  });
}

function ignoreEnterKey() {
  $(document).on('keypress', '#search_box', function (e) {
    if (e.which == 13) {
      return false;
    }
  });
}

$(document).ready(function () {
  initAuthenticationState();
  ignoreEnterKey();
  setFilterFromQueryString();
  refreshPlanning().done(function () {
    listenStarClicks();
    listenSearch();
    listenBackButton();
  });
});