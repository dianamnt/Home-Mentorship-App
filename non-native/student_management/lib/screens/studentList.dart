import 'package:flutter/material.dart';
import 'package:student_management/model/student.dart';
import 'package:student_management/screens/studentDetail.dart';
import 'package:student_management/util/dbhelper.dart';

class StudentList extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return StudentListState();
  }
}

class StudentListState extends State {
  DbHelper dbHelper = DbHelper();
  List<Student> students;
  int count = 0;

  @override
  Widget build(BuildContext context) {
    if (students == null) {
      students = List<Student>();
      getData();
    }

    return Scaffold(
      body: studentListItems(),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          goToDetail(Student('', '', ''));
        },
        tooltip: "Add new Student",
        child: new Icon(Icons.add),
      ),
    );
  }

  ListView studentListItems() {
    return ListView.builder(
      itemCount: count,
      itemBuilder: (BuildContext context, int position) {
        return Card(
          color: Colors.white,
          elevation: 2.0,
          child: ListTile(
            title: Text(this.students[position].name),
            subtitle: Text(this.students[position].place),
            onTap: () {
              debugPrint("Tapped on " + this.students[position].id.toString());
              goToDetail(this.students[position]);
            },
          ),
        );
      },
    );
  }

  void getData() {
    final dbFuture = dbHelper.initDatabase();
    dbFuture.then((result) {
      final studentFuture = dbHelper.getStudents();
      studentFuture.then((result) {
        List<Student> studentList = List<Student>();
        count = result.length;
        for (int i = 0; i < count; i++) {
          studentList.add(Student.fromObject(result[i]));
          debugPrint(studentList[i].name);
        }
        setState(() {
          students = studentList;
          count = count;
        });
        debugPrint("Items " + count.toString());
      });
    });
  }

  void goToDetail(Student student) async {
    bool result = await Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => StudentDetail(student)),
    );
    if (result == true) {
      getData();
    }
  }
}
