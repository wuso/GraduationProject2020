$(".panel-heading").on("click", function(e) {
    var idLength = e.currentTarget.id.length;
    var index = e.currentTarget.id.substr(idLength - 1, idLength);
    $("#sub" + index).on('hidden.bs.collapse', function() {
        $(e.currentTarget).find("span").removeClass("glyphicon glyphicon-triangle-bottom");
        $(e.currentTarget).find("span").addClass("glyphicon glyphicon-triangle-right");
    })
    $("#sub" + index).on('shown.bs.collapse', function() {
        $(e.currentTarget).find("span").removeClass("glyphicon glyphicon-triangle-right");
        $(e.currentTarget).find("span").addClass("glyphicon glyphicon-triangle-bottom");
    })
})

$(".panel-body > .nav > li > a").on("click", function(e) {
    alert(e.currentTarget.textContent);
});

function download(){
    var html = $("#results").html();
    $("#dataTable").val(html);
    $("#exportExcle").submit();
}
function download1(){
    var html = $("#results").html();
    $("#dataTable").val(html);
    $("#exportExcle").submit();
}
/**
 * @return {string}
 */
function GetQueryString(key)
{
    // 获取参数
    let url = window.location.search;
    // 正则筛选地址栏
    let reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)");
    // 匹配目标参数
    let result = url.substr(1).match(reg);
    //返回参数值
    return result ? decodeURIComponent(result[2]) : null;
}
$("#fileImport1").click(function () {
    $("#file").click();
});