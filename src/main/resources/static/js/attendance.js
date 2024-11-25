let calendar; // FullCalendar 객체를 저장할 변수
let consecutiveDays = 0; // 연속 출석 일수를 저장할 변수
let points = 0; // 출석 포인트를 저장할 변수

$(document).ready(function () {
    checkSessionAndInitialize();
});
function checkSessionAndInitialize() {
    $.ajax({
        url: '/api/checkSession',
        method: 'GET',
        success: function (response) {
            if (response === 'success') {
                initializeFullCalendar();
                loadAttendanceDatesFromServer();
                loadPointsFromLocalStorage();
                updateStreakCount();
                updatePointsDisplay(points);
            } else {
                alert('로그인이 필요합니다.');
                window.location.href = '/login.html';
            }
        },
        error: function (xhr, status, error) {
            console.error('세션 체크 오류:', error);
            alert('세션 체크 중 오류가 발생했습니다.');
        }
    });
}

function loadPointsFromLocalStorage() {
    const storedPoints = localStorage.getItem('attendancePoints');
    if (storedPoints !== null) {
        points = parseInt(storedPoints, 10);
    }
}

function savePointsToLocalStorage() {
    localStorage.setItem('attendancePoints', points.toString());
}

function loadAttendanceDatesFromServer() {
    $.ajax({
        url: '/api/getAttendanceDates',
        method: 'GET',
        success: function (dates) {
            if (Array.isArray(dates)) {
                sessionStorage.setItem('attendanceDates', JSON.stringify(dates));
                loadAttendanceDatesFromSessionStorage();
                updateStreakCount();
            } else {
                console.error('출석 데이터 불러오기 실패: 데이터가 배열이 아닙니다.', dates);
            }
        },
        error: function (xhr, status, error) {
            console.error('출석 데이터 불러오기 오류:', error);
        }
    });
}
function initializeFullCalendar() {
    const calendarEl = document.getElementById('calendar');
    calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        locale: 'en',
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth'
        },
        events: function (fetchInfo, successCallback, failureCallback) {
            fetch('/api/events')
                .then(response => response.json())
                .then(data => {
                    const events = data.map(event => ({
                        title: event.title,
                        start: event.eDate
                    }));

                    const storedDates = JSON.parse(sessionStorage.getItem('attendanceDates')) || [];
                    storedDates.forEach(date => {
                        if (!events.some(event => event.start === date)) {
                            events.push({
                                start: date,
                                rendering: 'background',
                                color: 'transparent',
                                textColor: 'transparent',
                                borderColor: 'transparent',
                                className: 'attendance-stamp'
                            });
                        }
                    });

                    successCallback(events);
                })
                .catch(error => failureCallback(error));
        }
    });

    calendar.render();
}

function performAttendanceCheck() {
    const today = new Date().toISOString().split('T')[0];
    console.log('오늘 날짜:', today); // 디버깅용 콘솔 로그 추가

    $.ajax({
        url: '/api/attendance',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ date: today }),
        success: function (data) {
            console.log('출석 체크 요청 성공:', data); // 디버깅용 콘솔 로그 추가
            if (data.success) {
                storeAttendanceDate(today);
                points = calculatePoints();
                savePointsToLocalStorage();
                updatePointsDisplay(points);
                alert('출석 체크 완료! 포인트: ' + points);
            } else {
                alert('출석 체크 실패!');
            }
        },
        error: function (xhr, status, error) {
            console.error('AJAX 오류:', error);
            alert('출석 체크 요청 중 오류가 발생했습니다.');
        }
    });
}

function storeAttendanceDate(date) {
    let storedDates = JSON.parse(sessionStorage.getItem('attendanceDates')) || [];
    console.log('저장된 출석 날짜들:', storedDates); // 디버깅용 콘솔 로그 추가

    if (!storedDates.includes(date)) {
        storedDates.push(date);
        sessionStorage.setItem('attendanceDates', JSON.stringify(storedDates));
        addAttendanceEvent(date);

        $.ajax({
            url: '/api/saveAttendanceDate',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ date: date }),
            success: function (response) {
                console.log('서버에 출석 날짜 저장 성공:', response); // 디버깅용 콘솔 로그 추가
                if (response.success) {
                    updateStreakCount();
                    points = calculatePoints();
                    savePointsToLocalStorage();
                    updatePointsDisplay(points);
                    alert('출석 체크 완료! 포인트: ' + points);
                } else {
                    console.error('서버에 출석 날짜 저장 실패:', response.error || 'Unknown error');
                    alert('서버에 출석 날짜 저장 실패!');
                }
            },
            error: function (xhr, status, error) {
                console.error('서버에 출석 날짜 저장 요청 실패:', error);
                alert('서버에 출석 날짜 저장 요청 실패!');
            }
        });
    } else {
        alert('이미 출석체크된 날짜입니다: ' + date);
    }
}



function updateStreakCount() {
    const storedDates = JSON.parse(sessionStorage.getItem('attendanceDates')) || [];
    const today = new Date().toISOString().split('T')[0];

    consecutiveDays = 0;
    let currentDate = new Date(today);

    while (storedDates.includes(currentDate.toISOString().split('T')[0])) {
        consecutiveDays++;
        currentDate.setDate(currentDate.getDate() - 1);
    }

    updateStreakDisplay();
}

function calculatePoints() {
    const basePoints = 10;
    return basePoints * consecutiveDays;
}

function updatePointsDisplay(points) {
    const pointsDisplayElement = document.getElementById('points-display');
    if (pointsDisplayElement) {
        pointsDisplayElement.textContent = points;
    } else {
        console.error('id가 "points-display"인 요소를 찾을 수 없습니다.');
    }
}

function updateStreakDisplay() {
    const attendanceCountElement = document.getElementById('atckCnt');
    if (attendanceCountElement) {
        attendanceCountElement.textContent = consecutiveDays;
    } else {
        console.error('id가 "atckCnt"인 요소를 찾을 수 없습니다.');
    }
}
function addAttendanceEvent(date) {
    // 주어진 날짜에 출석 이벤트를 FullCalendar에 추가하는 함수
    if (!checkIfEventExists(date)) {
        calendar.addEvent({
            start: date,
            allDay: true,
            rendering: 'background', // 배경으로 표시
            color: 'transparent', // 배경 색상 투명
            textColor: 'transparent', // 텍스트 색상 투명
            borderColor: 'transparent', // 테두리 색상 투명
            className: 'attendance-stamp' // 클래스 이름 지정
        });
    }
}

function checkIfEventExists(date) {
    // FullCalendar에 이미 해당 날짜의 이벤트가 있는지 확인
    const events = calendar.getEvents();
    return events.some(event => {
        const eventDate = event.start.toISOString().split('T')[0];
        return eventDate === date;
    });
}


function loadAttendanceDatesFromSessionStorage() {
    const storedDates = JSON.parse(sessionStorage.getItem('attendanceDates')) || [];
    storedDates.forEach(date => {
        addAttendanceEvent(date);
    });
}
