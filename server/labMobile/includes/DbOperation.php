<?php
 
class DbOperation
{
    private $con;
 
    function __construct()
    {
        require_once dirname(__FILE__) . '/DbConnect.php';
        $db = new DbConnect();
        $this->con = $db->connect();
    }
 
	//adding a record to database 
	public function createStudent($name, $location, $dateTime){
		$stmt = $this->con->prepare("INSERT INTO student (name, location, dateTime) VALUES (?, ?, ?)");
		$stmt->bind_param('sss', $name, $location, $dateTime);
		if($stmt->execute())
			return true; 
		return false; 
	}

	//deleting a student from db
	public function deleteStudent($name) {
		$stmt = $this->con->prepare("DELETE FROM student WHERE name = ?");
		$stmt->bind_param('s', $name);
		if($stmt->execute())
			return true; 
		return false; 
	}

	//updating a student from db
	public function updateStudent($oldname, $name, $location, $dateTime) {
		$stmt = $this->con->prepare("UPDATE student SET name=?, location=?, dateTime=? WHERE name=?");
		$stmt->bind_param('ssss', $name, $location, $dateTime, $oldname);
		if($stmt->execute())
			return true; 
		return false; 
	}
	
	//fetching all records from the database 
	public function getStudents(){
		$stmt = $this->con->prepare("SELECT name, location, dateTime FROM student");
		$stmt->execute();
		$stmt->bind_result($name, $location, $dateTime);
		$students = array();
		
		while($stmt->fetch()){
			$temp = array(); 
			$temp['name'] = $name; 
			$temp['location'] = $location; 
			$temp['dateTime'] = $dateTime; 
			array_push($students, $temp);
		}
		return $students; 
	}
}