function showPassword() {
    let passwordInput = document.getElementById("password");
    let confirmPasswordInput = document.getElementById("confirmPassword");

    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        confirmPasswordInput != null ? confirmPasswordInput.type = "text" : '';
    } else {
        passwordInput.type = "password";
        confirmPasswordInput != null ? confirmPasswordInput.type = "password" : '';
    }
}