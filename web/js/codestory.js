var dayLabels = {
  '2012-11-12':'Monday, Nov. 12 2012',
  '2012-11-13':'Tuesday, Nov. 13 2012',
  '2012-11-14':'Wednesday, Nov. 14 2012',
  '2012-11-15':'Thursday, Nov. 15 2012',
  '2012-11-16':'Friday, Nov. 16 2012'
}

function set_content(selector, template, data) {
  $(selector).html(Hogan.compile($(template).html()).render(data));
}

function initAuthenticationState() {
  var authenticated = ($.cookie('userId') != null);
  set_content('#auth', '#header-template', { 'authenticated': authenticated });
}

function refreshRegistrationLinks() {
  $('.register').html('Register');
  $('.talk').removeClass('registered');

  var authenticated = ($.cookie('userId') != null);
  if (authenticated) {
    $.getJSON('registrations', function (json) {
      _.each(json, function (talkId) {
        $('#session-' + talkId + ' .register').html('Unregister');
        $('#session-' + talkId).addClass('registered');
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
    refreshRegistrationLinks();
  });
}

function listenRegistrationClicks() {
  $(document).on('click', '.register', function () {
    var talkId = $(this).attr('data-talk');

    if ($(this).html() == "Register") {
      $.post('register', { talkId:talkId }, refreshRegistrationLinks);
    } else {
      $.post('unregister', { talkId:talkId }, refreshRegistrationLinks);
    }

    return false;
  });
}

function listenSearch() {
  $('#search input').keyup(function () {
    $('.day_wrapper, .toc-link, .slot, .talk').show();

    var text = $(this).val();
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

    $('.day_wrapper:not(:has(.talk:visible))').hide();
    $('.slot:not(:has(.talk:visible))').hide();
    $('.hour a:hidden').each(function () {
      $('.toc-link[data-toc="' + this.id + '"]').hide();
    })
  })
}

$(document).ready(function () {
  initAuthenticationState();
  refreshPlanning();
  listenRegistrationClicks();
  listenSearch();
})