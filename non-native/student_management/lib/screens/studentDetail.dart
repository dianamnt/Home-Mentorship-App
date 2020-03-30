import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:student_management/model/student.dart';
import 'package:student_management/util/dbhelper.dart';

DbHelper dbHelper = DbHelper();

final List<String> options = const <String>['Save', 'Delete', 'Back'];

const menuSave = 'Save';
const menuDelete = 'Delete';
const menuBack = 'Back';

class StudentDetail extends StatefulWidget {
  final Student student;
  StudentDetail(this.student);

  @override
  State<StatefulWidget> createState() {
    return StudentDetailState(student);
  }
}

class StudentDetailState extends State {
  Student student;

  StudentDetailState(this.student);

  TextEditingController nameCtrl = TextEditingController();
  TextEditingController placeCtrl = TextEditingController();
  TextEditingController dateTimeCtrl = TextEditingController();

  @override
  Widget build(BuildContext context) {
    nameCtrl.text = student.name;
    placeCtrl.text = student.place;
    dateTimeCtrl.text = student.dateTime;
    TextStyle textStyle = Theme.of(context).textTheme.title;
    return Scaffold(
      appBar: AppBar(
        automaticallyImplyLeading: false,
        actions: <Widget>[
          PopupMenuButton<String>(
            onSelected: select,
            itemBuilder: (BuildContext context) {
              return options.map((String option) {
                return PopupMenuItem<String>(
                  value: option,
                  child: Text(option),
                );
              }).toList();
            },
          )
        ],
      ),
      body: Column(
        children: <Widget>[
          TextField(
            controller: nameCtrl,
            style: textStyle,
            onChanged: (value) => this.updateName(),
            decoration: InputDecoration(
                labelText: "Name",
                labelStyle: textStyle,
                border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(5.0))),
          ),
          TextField(
            controller: placeCtrl,
            style: textStyle,
            onChanged: (value) => this.updatePlace(),
            decoration: InputDecoration(
                labelText: "Place",
                labelStyle: textStyle,
                border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(5.0))),
          ),
          TextField(
            controller: dateTimeCtrl,
            style: textStyle,
            onChanged: (value) => this.updateDateTime(),
            decoration: InputDecoration(
                labelText: "DateTime",
                labelStyle: textStyle,
                border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(5.0))),
          ),
        ],
      ),
    );
  }

  void select (String value) async {
    int result; 
    switch (value) {
      case menuSave:
        save(); 
        break;
      case menuDelete:
        if (student.id == null) {
          return;
        }
        result = await dbHelper.deleteStudent(student.id.toInt());
        break;
      case menuBack: 
        Navigator.pop(context, true);
        break;
    }
  }

  void save() {
    if (student.id != null) {
      dbHelper.updateStudent(student);
    }
    else {
      dbHelper.insertStudent(student);
    }
    Navigator.pop(context, true);
  }

  void updateName() {
    student.name = nameCtrl.text;
  }

  void updatePlace() {
    student.place = placeCtrl.text;
  }

  void updateDateTime() {
    student.dateTime = dateTimeCtrl.text;
  }

}
