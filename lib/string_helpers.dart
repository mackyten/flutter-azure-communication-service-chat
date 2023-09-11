import 'package:intl/intl.dart';

String twelveHourFormat(String dateString) {
  DateTime dateTime = DateTime.parse(dateString);

  DateFormat dateFormat = DateFormat('yyyy-MM-dd hh:mm a');

  String formattedDate = dateFormat.format(dateTime);

  return formattedDate;
}
