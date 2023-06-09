function updateBusiness() {
    const uuid = document.getElementById("uuid").value;
    const seat = document.getElementById("seatAmount").value;
    const name = document.getElementById("name").value;
    const url = `${baseUrl}/api/auth/register/api/admin/busniess/${uuid}`

    let payload = {
        "name": name,
        "seatBaseline": seat
    };

    fetch(url, {
        method: "PATCH",
        headers: {

            'Authorization': `Bearer ${window.localStorage.getItem("jwt")}`,
            "Content-Type": "application/json",
        },
        body: JSON.stringify(payload)
    })
        .then(response => response.json())
        .then(() => {
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
 
    
