
$(function() {
    $("#loginForm").on("submit", (function() {
        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            timeout: 2000,
            error: function(errorObject) {
                console.error("Failed to login !");
                $("#error-placeholder").text(errorObject.responseText)
            },
            success: function(serverResponse) {
                myRedirect(serverResponse);
            }
        });

        return false;
    }));
});