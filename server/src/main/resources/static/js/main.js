var oMenu = document.getElementById('menu');
var oBtn = oMenu.getElementsByTagName('a')[0];
var oLeftBar = document.getElementById('leftBar');
oBtn.onclick = function () {
    if (oLeftBar.offsetLeft === 0) {
        oLeftBar.style.left = -249 + 'px';
    } else {
        oLeftBar.style.left = 0 + 'px';
    }
    if (document.documentElement.clientWidth <= 481) {
        document.onclick = function () {
            if (oLeftBar.offsetLeft === 0) {
                console.log(123);
                oLeftBar.style.left = -249 + 'px';
            }
        }
    }
};

var oNavItem = document.getElementById('navItem');
var aA = oNavItem.getElementsByTagName('a');
for (var i = 0; i < aA.length; i++) {
    aA[i].onclick = function () {
        for (var j = 0; j < aA.length; j++) {
            aA[j].className = '';
        }
        this.className = 'active';
    }
}

$(window).scroll(function () {
    if ($(window).scrollTop() >= 200) {
        $('#fixedBar').fadeIn(300);
    } else {
        $('#fixedBar').fadeOut(300);
    }
});

$('#fixedBar').click(function () {
    $('html,body').animate({scrollTop: '0px'}, 800);
});