class Student {
  int id;
  String name;
  String place;
  String dateTime;

  Student(this.name, this.place, this.dateTime);
  Student.withId(this.id, this.name, this.place, this.dateTime);

  Map<String, dynamic> toMap() {
    var map = Map<String, dynamic>();
    map["id"] = id;
    map["name"] = name;
    map["place"] = place;
    map["dateTime"] = dateTime;
    return map;
  }

  Student.fromObject(dynamic o) {
    this.id = o["id"];
    this.name = o["name"];
    this.place = o["place"];
    this.dateTime = o["dateTime"];
  }
}

