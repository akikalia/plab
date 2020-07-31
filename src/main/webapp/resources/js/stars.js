
document.addEventListener('DOMContentLoaded', addListeners);

var filledStar = '/resources/icons/filled_star.png';
var emptyStar = '/resources/icons/empty_star.png' ;
var filledUserStar = '/resources/icons/filled_user_star.png';

function addListeners() {
    let posts = document.querySelectorAll('.post');
    for (let i = 0; i < posts.length; i++) {
        let stars_div = posts[i].querySelectorAll('.stars')[0];
        stars_div.addEventListener('mouseout', resetStars);
        resetStars.call(stars_div);
        let stars = posts[i].querySelectorAll('.star');
        for (let i = 0; i < stars.length; i++) {
            stars[i].addEventListener('click', setRating);
            stars[i].addEventListener('mouseover', setStars);
        }
    }
}
function resetStars(){
    let rating = parseInt(this.getAttribute('data-rating'));
    let userRating = parseInt(this.getAttribute('user-rating'));

    if (userRating > 0){
        rating = userRating;
    }
    let stars = this.querySelectorAll('.star');
    for (let i = 0; i < stars.length; i++) {
        if (rating > 0){
            if (userRating > 0)
                stars[i].setAttribute('src', filledUserStar);
            else
                stars[i].setAttribute('src', filledStar);
        }
        else
            stars[i].setAttribute('src', emptyStar);
        rating--;
    }
}

function setStars() {
    let stars = this.parentElement.querySelectorAll('.star');
    let starFilled = filledUserStar;
    for (let i = 0; i < stars.length; i++) {
        stars[i].setAttribute('src', starFilled);
        if (stars[i].isSameNode(this)){
            starFilled = emptyStar;
        }
    }
}
function setRating() {
    let input = this.parentElement.parentElement.querySelectorAll('.rating-input');
    input[0].setAttribute('value', this.getAttribute('val'));
    input[0].parentElement.submit();
    resetStars();
    return;
}