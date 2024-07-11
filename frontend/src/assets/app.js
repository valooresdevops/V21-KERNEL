function toggleSideNav() {
    if($('.sideBarContainer').attr("class").indexOf("hide") != -1)
    {
        $('.sideBarContainer').removeClass("hide");
        $('.sideBarContainer').addClass("show");
    }
    else
    { 
        $('.sideBarContainer').removeClass("show");
        $('.sideBarContainer').addClass("hide");
    }
}