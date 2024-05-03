$(document).ready(function() {
    
    

    populateTable();

    $('#table-body').on('click', '.edit-button', function() {
        var userId = $(this).data('userid');
        var firstname = $(this).data('firstname');
        var lastname = $(this).data('lastname');
        var email = $(this).data('email');
        var phone = $(this).data('phone');

        $('#editUserId').val(userId);
        $('#editFirstName').val(firstname);
        $('#editLastName').val(lastname);
        $('#editEmail').val(email);
        $('#editPhoneNumber').val(phone);
    });

    $('#table-body').on('click', '.delete-button', function() {
        var userId = $(this).data('userid');
       

        $('#userIdDelete').val(userId);
     
    });

    $('#saveChangesEdit').click(function() {
        var userId = $('#editUserId').val();
        var firstname = $('#editFirstName').val();
        var lastname = $('#editLastName').val();
        var email = $('#editEmail').val();
        var phone = $('#editPhoneNumber').val();


        
        $.ajax({
            url: '/admin/updateUser', 
            type: 'PUT',
            data: {
                userId: userId,
                firstname: firstname,
                lastname: lastname,
                email: email,
                phone: phone
            },
            success: function(response) {
                swal("Successful!", "user account update successfully!", "success").then((value) => {
                    window.location.reload();
                  });
            },
            error: function(error) {
                console.log('Error:', error);
            }
        });
    });

    $('#saveChangesCreate').click(function() {
        var firstname = $('#createFirstName').val();
        var lastname = $('#createLastName').val();
        var email = $('#createEmail').val();
        var phone = $('#createPhoneNumber').val();
        var data = {
            firstName: firstname,
            lastName: lastname,
            email: email,
            phone: phone,
            registrationType: "Admin"
          };
          $.ajax({
            type: 'POST',
            url: '/register/invite',
            data: JSON.stringify(data),
            contentType: 'application/json',
            success: function(response) {
                swal("Successful!", "user account create successfully!", "success").then((value) => {
                    window.location.reload();
                  });
            },
            error: function(xhr, status, error) {
                if (xhr.status === 500) {
                    swal("Failed updating!", "Internal Server Error: \n" + xhr.responseText , "error");
                } else if (xhr.status === 404) {
                    swal("Failed updating!", "Not Found", "error");
                } else if (xhr.status === 400) {
                  var showMessage="";
                  var errorResponse = JSON.parse(xhr.responseText);
                  var errorMessages = [];
        
                  if (Array.isArray(errorResponse.errors)) {
        
                    errorResponse.errors.forEach(function(error) {
                      errorMessages.push(error.defaultMessage);
                    });
                  } else {
                    errorMessages.push(errorResponse);
                  }
        
                  errorMessages.forEach(function(errorMessage) {
                    showMessage += errorMessage+"\n";
                  });
                  swal("Failed updating!", showMessage, "error");
                }else{
                  swal("Failed updating!", "Error updating course: " + error, "error");
                }
            }
        });
    });
});

function populateTable(){
    $.ajax({
        url: '/admin/list',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            populateTableWithData(data);
        },
        error: function(error) {
            console.log('Error:', error);
        }
    });
}

function populateTableWithData(data) {
    var tableBody = $('#table-body');
    tableBody.empty();

    $.each(data, function(index, admin) {
        var row = '<tr>' +
        '<td>' + admin.id + '</td>' +
        '<td>' + admin.firstName + '</td>' +
        '<td>' + admin.lastName +'</td>' +
        '<td>' + admin.email + '</td>' +
        '<td>' + admin.phone + '</td>' +
        '<td><button class="edit-button" data-toggle="modal" data-target="#editModal" data-userid="' + admin.id + '" data-firstname="' + admin.firstName + '" data-lastname="' +  admin.lastName + '" data-email="' + admin.email + '" data-phone="' + admin.phone + '"><i class="fas fa-edit"></i></button></td>' +
        '<td><button class="delete-button" data-toggle="modal" data-target="#deleteModal" data-userid="' + admin.id  + '"><i class="fas fa-trash-alt"></i></button></td>' +
        '</tr>';
        tableBody.append(row);
    });
}

//delete
$(document).ready(function() {
    $('#deleteButton').click(function() {
        var userId = $('#userIdDelete').val();
        $.ajax({
            url: '/admin/deleteUser/' + userId,
            type: 'DELETE',
            dataType: 'json',
            success: function(response) {
                swal({
                    title: "Successful!",
                    text: "User account created successfully!",
                    icon: "success",
                  }).then((value) => {
                    window.location.reload();
                  });
            },
            error: function(error) {
                swal({
                    title: "Successful!",
                    text: "User account created successfully!",
                    icon: "success",
                  }).then((value) => {
                    window.location.reload();
                  });
            }
        });
    });
  });

  