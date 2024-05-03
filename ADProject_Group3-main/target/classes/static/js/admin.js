// Function to populate the edit modal with admin details
var adminId;
function populateEditModal(adminId,firstName,lastName,email,phone) {
    $("#adminIdUpdate").val(adminId);
    $('#firstNameUpdate').val(firstName);
    $('#lastNameUpdate').val(lastName);
    $('#emailUpdate').val(email);
    $('#phoneUpdate').val(phone);
    // $('#userNumberUpdate').val(userNumber);
  }
//data binding
  $(document).ready(function () {
    // Handle click event on Edit button
    $('[data-target="#editModal"]').click(function () {
      adminId = $(this).data("adminid");
      var firstName = $(this).data("firstname");
      var lastName = $(this).data("lastname");
      var email = $(this).data("email");
      var phone = $(this).data("phone");
      // var userNumber = $(this).data("usernumber");
      populateEditModal(adminId,firstName,lastName,email,phone);
    });

    $('[data-target="#deleteModal"]').click(function () {
      adminId = $(this).data("adminid");
      $("#adminIdDelete").val(adminId);
    });
  });

  // $(document).ready(function() {
  //   $('#adminDataTable').DataTable({
  //     dom: 'Bfrtip',
  //     buttons: [
  //       'copy', 'csv', 'excel', 'pdf', 'print'
  //     ]
  //   });
  // });
  $(document).ready(function() {
    $('#adminDataTable').DataTable({
      'aoColumnDefs':[{'bSortable':false,'aTargets':[-1,-2]}],
      dom: '<"top"lfB>rt<"bottom"ip><"clear">',
      buttons: [
        {
          extend: 'copy',
          className: 'custom-copy-btn btn-icon',
          text: '<i class="fas fa-copy"></i> Copy',
        },
        {
          extend: 'csv',
          className: 'custom-csv-btn btn-icon',
          text: '<i class="fas fa-file-csv"></i> CSV',
        },
        {
          extend: 'excel',
          className: 'custom-excel-btn btn-icon',
          text: '<i class="fas fa-file-excel"></i> Excel',
        },
        {
          extend: 'pdf',
          className: 'custom-pdf-btn btn-icon',
          text: '<i class="fas fa-file-pdf"></i> PDF',
        },
        {
          extend: 'print',
          className: 'custom-print-btn btn-icon',
          text: '<i class="fas fa-print"></i> Print',
        }
      ],
      scrollX: true
    });
  });

//create
$(document).ready(function() {
  $('#createButton').click(function() {
    var firstName = $('#firstNameCreate').val();
    var lastName = $('#lastNameCreate').val();
    var email = $('#emailCreate').val();
    var phone = $('#phoneCreate').val();
    var data = {
      firstName: firstName,
      lastName: lastName,
      email: email,
      phone: phone,
      registrationType: "Admin"
    };

    // // Show loading state
    // var loadingMessage = "Creating admin...";
    // swal({
    //   title: loadingMessage,
    //   buttons: false,
    //   closeOnClickOutside: false,
    //   closeOnEsc: false,
    //   icon: "info"
    // });
  // Create a new Spinner instance
var spinner = new Spinner().spin();

// Display the spinner
document.getElementById('loader').appendChild(spinner.el);

    // Send the data to the server using AJAX
    $.ajax({
      type: 'POST',
      // url: '/admin/admin/create', 
      url: '/register/invite',
      data: JSON.stringify(data),
      contentType: 'application/json',
      success: function(response) {
        // console.log('admin created successfully');
        // window.location.reload();
        spinner.stop();
        swal("Successful!", firstName+" "+lastName+" has been notified to complete account creation!", "success").then((value) => {
          window.location.reload();
        });
      },
      error: function(xhr, status, error) {
       //swal.close();
       spinner.stop();
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

//update
$(document).ready(function() {
  $('#updateButton').click(function() {
    var adminId =  $('#adminIdUpdate').val();
    var firstName = $('#firstNameUpdate').val();
    var lastName = $('#lastNameUpdate').val();
    var email = $('#emailUpdate').val();
    var phone = $('#phoneUpdate').val();
    var data = {
      id: adminId,
      firstName: firstName,
      lastName: lastName,
      email: email,
      phone: phone
    };

    // Send the data to the server using AJAX
    $.ajax({
      type: 'POST',
      url: '/admin/admin/update', 
      data: JSON.stringify(data),
      contentType: 'application/json',
      success: function(response) {
        // console.log('admin updated successfully');
        // window.location.reload();
        swal("Successful!", "Create Successfully!", "success").then((value) => {
          window.location.reload();
        });
      },
      error: function(xhr, status, error) {
        if (xhr.status === 500) {
            swal("Failed updating!", "Internal Server Error", "error");
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

//delete
$(document).ready(function() {
  $('#deleteButton').click(function() {
    var adminId =  $('#adminIdDelete').val();
    var data = {
      id: adminId,
      firstName:  "",
      lastName:  "",
      email:  "",
      phone:  "",
    };

    $.ajax({
      type: 'DELETE',
      url: '/admin/admin/delete', 
      data: JSON.stringify(data),
      contentType: 'application/json',
      success: function(response) {
        // console.log('Course deleted successfully');
        // window.location.reload();
        swal("Successful!", "Delete Successfully!", "success").then((value) => {
          window.location.reload();
        });
      },
      error: function(xhr, status, error) {
        if (xhr.status === 500) {
            swal("Failed updating!", "Internal Server Error", "error");
        } else if (xhr.status === 404) {
            swal("Failed updating!", "Not Found", "error");
        } else {
            swal("Failed updating!", "Error updating course: " + error, "error");
        }
    }
    });

    
  });
});

