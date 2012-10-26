var dayLabels = {
  '2012-11-12':'Monday, Nov. 12 2012',
  '2012-11-13':'Tuesday, Nov. 13 2012',
  '2012-11-14':'Wednesday, Nov. 14 2012',
  '2012-11-15':'Thursday, Nov. 15 2012',
  '2012-11-16':'Friday, Nov. 16 2012'
};

function set_content(selector, template, data) {
  $(selector).html(Hogan.compile($(template).html()).render(data));
}

function initAuthenticationState() {
  var authenticated = ($.cookie('userId') != null);
  set_content('#auth', '#header-template', { 'authenticated':authenticated });
}

function refreshStars() {
  $('.star').html('star');
  $('.talk').removeClass('starred');

  var authenticated = ($.cookie('userId') != null);
  if (authenticated) {
    $.getJSON('stars', function (json) {
      _.each(json, function (talkId) {
        $('#talk-' + talkId + ' .star').html('unstar');
        $('#talk-' + talkId).addClass('starred');
      });
    });
  }
}

function enrichPlanning(planning) {
  _.each(planning.days, function (day) {
    day.dayLabel = dayLabels[day.day];

    _.each(day.slots, function (slot) {
      _.each(slot.talks, function (talk) {
        talk.speakers_string = talk.speakers.join(', ');
      });
    });
  });
}

function refreshPlanning() {
  $.getJSON('planning.json', function (json) {
    enrichPlanning(json);
    set_content('#content', '#talks-template', json);
    refreshStars();
  });
}

function listenStarClicks() {
  $(document).on('click', '.star', function () {
    var talkId = $(this).attr('data-talk');

    if ($(this).html() == "star") {
      $.post('star', { talkId:talkId }, refreshStars);
    } else {
      $.post('unstar', { talkId:talkId }, refreshStars);
    }

    return false;
  });
}

function filterTalks() {
  $('.day_wrapper, .toc-link, .slot, .talk').show();

  var text = $('#search_box').val();
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

function listenSearch() {
  var searchTimer = null;

  $('#search_box').keyup(function () {
    if (searchTimer == null) {
      searchTimer = window.setTimeout(function () {
        searchTimer = null;
        filterTalks();
      }, 700);
    }
  });
}

$(document).ready(function () {
  initAuthenticationState();
  refreshPlanning();
  listenStarClicks();
  listenSearch();
});