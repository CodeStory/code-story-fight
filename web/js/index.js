$(function () {
  var previousScrollTop, currentScrollTop, yPosition = 0;
  $(window).scroll(function () {
    currentScrollTop = $(window).scrollTop();
    var diff = previousScrollTop - currentScrollTop;
    if (isNaN(diff)) {
      diff = 0;
    }
    yPosition = yPosition + diff;
    previousScrollTop = currentScrollTop;
    $('body').css('background-position', '0 ' + yPosition + 'px, ' + //
      '0 ' + yPosition * 2 + 'px, ' + //
      '0 ' + yPosition * 5 + 'px, ' + //
      '0 0');
  });
});