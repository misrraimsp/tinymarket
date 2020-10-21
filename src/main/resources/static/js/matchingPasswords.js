/*
    keep passwordConfirm pattern equals to password value
*/

'use strict';

document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("password").addEventListener("change", function() {
        document.getElementById("passwordConfirm").pattern = document.getElementById("password").value;
    }, false);
}, false);
