var current_login = 'dgageot';

function refreshRegistrationLinks() {
  $.getJSON('registrations/' + current_login, function (json) {
    $('.register').html('Register');

    _.each(json, function (talkId) {
      $('#session-' + talkId + ' a.register').html('Unregister');
    });
  });
}

function refreshPlanning() {
  $.getJSON('planning.json', function (json) {
    $("#content").html(Hogan.compile($('#talks-template').html()).render(json));
    refreshRegistrationLinks();
  });
}

function register(login, talkId, callback) {
  $.post('register', {login:login, talkId:talkId}, callback);
}

function unregister(login, talkId, callback) {
  $.post('unregister', {login:login, talkId:talkId}, callback);
}

function listenRegistrationClicks() {
  $('.register').live('click', function () {
    var talkId = $(this).attr('data-talk');

    if ($(this).html() == "Register") {
      register(current_login, talkId, refreshRegistrationLinks);
    } else {
      unregister(current_login, talkId, refreshRegistrationLinks);
    }
  });
}

function initAuthenticationState() {
  $('header').html(Hogan.compile($('#header-template').html()).render({authenticated:false}));
}

$(document).ready(function () {
  initAuthenticationState();
  refreshPlanning();
  listenRegistrationClicks();
});