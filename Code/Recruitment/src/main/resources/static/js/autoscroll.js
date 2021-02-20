$(document).ready(function() {
    // console.log(document.getElementsByClassName("autoScroll").length);
    if(document.getElementsByClassName("autoScroll").length > 0) {
        setTimeout(autoScroll, 1000);
    }
});

function autoScroll() {
    $('body,html').animate({scrollTop: 156}, 600);
}