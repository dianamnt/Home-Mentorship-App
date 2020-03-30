import 'package:sqflite/sqflite.dart';
import 'dart:async';
import 'dart:io';
import 'package:path_provider/path_provider.dart';
import 'package:student_management/model/student.dart';

class DbHelper {

  static final DbHelper helper = DbHelper.internal();

  String table = "student";
  String colid = "id";
  String colName = "name";
  String colPlace = "place";
  String colDateTime = "dateTime"; 

  DbHelper.internal();

  factory DbHelper() {
    return helper;
  }

  static Database _db;

  Future<Database> get db async {
    if (_db == null) {
      _db = await initDatabase();
    }
    return _db;
  }

  Future<Database> initDatabase() async {
    Directory dir = await getApplicationDocumentsDirectory();
    String path = dir.path + "studentManagement.db";
    var dbStudentManagement = await openDatabase(path, version: 1, onCreate: createDb);
    return dbStudentManagement;
  }

  void createDb(Database db, int newVersion) async {
    await db.execute(
        "Create table $table($colid INTEGER PRIMARY KEY, $colName TEXT, $colPlace TEXT, $colDateTime TEXT)");
  }

  Future<int> insertStudent(Student student) async {
    Database db = await this.db;
    var result = await db.insert(table, student.toMap());
    return result;
  }

  Future<List> getStudents() async {
    Database db = await this.db;
    var result = await db.rawQuery("SELECT * FROM $table");
    return result;
  }

  Future<int> getStudentCount() async {
    Database db = await this.db;
    var result = Sqflite.firstIntValue(
        await db.rawQuery("SELECT count (*) from $table"));
    return result;
  }

  Future<int> updateStudent(Student student) async {
    var db = await this.db;
    var result = await db
        .update(table, student.toMap(), where: "$colid = ?", whereArgs: [student.id]);
    return result;
  }

  Future<int> deleteStudent(int id) async {
    int result;
    var db = await this.db;
    result = await db.rawDelete("DELETE FROM $table WHERE $colid = $id");
    return result;
  }

}

