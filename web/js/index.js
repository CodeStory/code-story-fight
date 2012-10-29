$(document).ready(function () {
  $('#toggle').click(function () {
    $('#front').slideToggle('slow');
    $('#back').toggle();
  });

  $('#counter').countdown({
    timestamp:(new Date(2012, 10, 12, 8, 0, 0, 0)).getTime()
  });

  $('#score_left').text(Math.floor((Math.random() * 99) + 1));
  $('#score_right').text(Math.floor((Math.random() * 99) + 1));
});
