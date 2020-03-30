<?php

//adding dboperation file 
require_once '../includes/DbOperation.php';

//response array 
$response = array();

//if a get parameter named op is set we will consider it as an api call 
if (isset($_GET['op'])) {

	//switching the get op value 
	switch ($_GET['op']) {

			//Add a student
		case 'addstudent':

			$json = file_get_contents('php://input');
			$data = json_decode($json);
			$name = $data->name;
			$location = $data->location;
			$dateTime = $data->dateTime;

			if (isset($name) && isset($location) && isset($dateTime)) {
				$db = new DbOperation();
				if ($db->createStudent($name, $location, $dateTime)) {
					$response['error'] = false;
					$response['message'] = 'Student added successfully';
				} else {
					$response['error'] = true;
					$response['message'] = 'Could not add student';
				}
			} else {
				$response['error'] = true;
				$response['message'] = 'Required Parameters are missing';
			}
			break;

			//Delete a student
		case 'deletestudent':
			$json = file_get_contents('php://input');
			$data = json_decode($json);
			$name = $data->name;
			$db = new DbOperation();
			if ($db->deleteStudent($name)) {
				$response['error'] = false;
				$response['message'] = 'Student deleted successfully';
			} else {
				$response['error'] = true;
				$response['message'] = 'Could not delete student';
			}
			break;

			//Update a student
		case 'updatestudent':
			$json = file_get_contents('php://input');

			// Converts it into a PHP object
			$data = json_decode($json);
			$oldname = $data->oldname;
			$name = $data->name;
			$location = $data->location;
			$dateTime = $data->dateTime;			
			if (isset($oldname) && isset($name) && isset($location) && isset($dateTime)) {
				$db = new DbOperation();
				if ($db->updateStudent($oldname, $name, $location, $dateTime)) {
					$response['error'] = false;
					$response['message'] = 'Student updated successfully';
				} else {
					$response['error'] = true;
					$response['message'] = 'Could not update student';
				}
			} else {
				$response['error'] = true;
				$response['message'] = 'Required Parameters are missing';
			}
			break;

			//Display students
		case 'getstudents':
			$db = new DbOperation();
			$students = $db->getStudents();
			if (count($students) <= 0) {
				$response['error'] = true;
				$response['message'] = 'Nothing found in the database';
			} else {
				$response['error'] = false;
				$response['students'] = $students;
			}
			break;

		default:
			$response['error'] = true;
			$response['message'] = 'No operation to perform';
	}
} else {
	$response['error'] = false;
	$response['message'] = 'Invalid Request';
}

//displaying the data in json 
echo json_encode($response);
