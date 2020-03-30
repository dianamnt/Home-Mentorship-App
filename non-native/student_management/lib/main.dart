import 'package:flutter/material.dart';
import 'package:student_management/screens/studentList.dart';

void main() {
  runApp(new StudentManagementApp());
}

class StudentManagementApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
  
    return MaterialApp(
      title: "Student Management",
      theme: new ThemeData(
        primarySwatch: Colors.deepOrange
      ),
      home: new MyHomePage(title: 'Student Management'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  State<StatefulWidget> createState() {
    return new MyHomePageState();
  }
}

class MyHomePageState extends State<MyHomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: new Text(widget.title),
      ),
      body: StudentList(),
    );
  }
}
