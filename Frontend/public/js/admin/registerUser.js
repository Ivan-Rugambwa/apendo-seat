function sendFormData() {
    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;
    const email = document.getElementById("email").value;
    const apiUrl = "http://wsprakt2.apendo.se:35462/api/admin/user";

    function requestBody(firstName, lastName, email, password) {
        console.log(firstName, lastName, email, password);
        let payload = {
            "firstName": firstName,
            "lastName": lastName,
            "email": email
        }
        return JSON.stringify(payload);
    };
    fetch(apiUrl, {
        method: "POST",
        body: requestBody(firstName, lastName, email, password),
        headers: {
            "Content-Type": "application/json",
        },
    })
        .then((response) => {
            console.log(response);
            // handle response here
        })
        .catch((error) => {
            console.error(error);
            // handle error here
        });
    

    
}



