fetch('https://ipinfo.io/json?token=d0e51168751210')
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        // IP 주소 및 추가 정보를 가져와서 페이지에 표시합니다.
        document.getElementById('ip-address').textContent = `IP 주소: ${data.ip}`;
        document.getElementById('ip-location').textContent = `위치: ${data.city}, ${data.region}, ${data.country}`;
        document.getElementById('ip-isp').textContent = `ISP: ${data.org}`;
    })
    .catch(error => {
        // 에러가 발생하면 콘솔에 에러를 출력하고 사용자에게 에러 메시지를 표시합니다.
        console.error('Error fetching IP address:', error);
        document.getElementById('ip-address').textContent = 'Error fetching IP address: ' + error.message;
    });