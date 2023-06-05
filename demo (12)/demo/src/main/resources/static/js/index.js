
function getAllClasses() {
    let x = 0;
    $.ajax({
        url: "http://localhost:8086/api/class",
        success: response => {
            console.log(response.length);
            while (x < response.length) {
                $('#classTable').append(`<tr class="tr"><td>${response[x].id}</td>
                                 <td>${response[x].numberOfStudents}</td>
                                 <td>${response[x].classAvg}</td>
                                 <td>${response[x].classRoomType}</td></tr>`
                )
                x++
            }
        }
    });
}

function getAllStudents() {
    let y = 0;
    $.ajax({
        url: "http://localhost:8086/api/student",
        success: response => {
            console.log(response.length);
            while (y < response.length) {
                $('#studentTable').append(`<tr class="tr"><td>${response[y].id}</td>
                                     <td>${response[y].lastName}</td>
                                     <td>${response[y].firstName}</td>
                                     <td>${response[y].avgGrade}</td>
                                     <td>${response[y].gender}</td>
                                     <td>${response[y].class_id}</td></tr>`
                )
                y++
            }
        }
    });
}

getAllClasses();
getAllStudents();