function setSizeLayout() {
    //得到浏览器窗口内部的尺寸
    var getBodyHeight = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
    var getBodyWidth = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
    document.getElementById("pageHeight").style.height = getBodyHeight + "px";
    document.getElementById("pageHeight").style.width = getBodyWidth * 0.70 + "px";
}


function landscapingInterface() {
    //  给登录注册按钮添加旋转功能,css3的变换和过渡实现
    var logReglis = document.getElementById("addOnMouseOver").getElementsByTagName("li");
    for (var i = 0; i < logReglis.length; i++) {
        logReglis[i].onmouseover = function () {
            this.style.cssText = "transform: rotate(-10deg);transition: transform 1s;" +
                "-moz-transition: transform 1s;-webkit-transition: transform 1s;-o-transition: transform 1s;";
        };
        logReglis[i].onmouseout = function () {
            this.style.cssText = "transform: rotate(0);transition: transform 1s;" +
                "-moz-transition: transform 1s;-webkit-transition: transform 1s;-o-transition: transform 1s;";
        };
    }

    // 给课程盒子添加动画效果
    try {
        var courseBoxLis = document.getElementById("courseBox").getElementsByTagName("li");
        for (var j = 0; j < courseBoxLis.length; j++) {
            courseBoxLis[j].onmouseover = function () {
                this.getElementsByTagName("a")[0].style.cssText = "animation:courseBgAddColor 0.1s;  -moz-animation:courseBgAddColor 0.1s;  " +
                    "-webkit-animation:courseBgAddColor 0.1s;  -o-animation:courseBgAddColor 0.1s;background-color: pink;width: 77%;height: 72%";
            };
            courseBoxLis[j].onmouseout = function () {
                this.getElementsByTagName("a")[0].style.cssText = "animation:courseBgRemoveColor 0.1s;  -moz-animation:courseBgRemoveColor 0.1s;  " +
                    "-webkit-animation:courseBgRemoveColor 0.1s;  -o-animation:courseBgRemoveColor 0.1s";
            }
        }
    } catch (err) {
        console.log("为了使代码继续运行");
    }
}

function addMenuNavUrl() {
    $(".menuNav ul li:nth-child(1) a").attr("href","baseInfo.html?courseID="+currentID);
   $(".menuNav ul li:nth-child(2) a").attr("href","previewData.html?courseID="+currentID);
    $(".menuNav ul li:nth-child(3) a").attr("href","electronicData.html?courseID="+currentID);
   $(".menuNav ul li:nth-child(4) a").attr("href","courseMessage.html?courseID="+currentID);
}












