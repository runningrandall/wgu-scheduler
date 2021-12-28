package com.randalladams.scheduler.util;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * A DateTimePicker with configurable datetime format where both date and time can be changed
 * via the text field and the date can additionally be changed via the JavaFX default date picker.
 *
 * This DateTimePicker was taken from https://github.com/edvin/tornadofx-controls/
 * I had to copy it rather than import it as Maven dependency due to this bug:
 * https://github.com/edvin/tornadofx-controls/issues/12
 */
public class DateTimePicker extends DatePicker {
  public static final String DefaultFormat = "yyyy-MM-dd HH:mm";

  private DateTimeFormatter formatter;
  // private final ObjectProperty<LocalDateTime> dateTimeValue = new SimpleObjectProperty<>(LocalDateTime.now());
  private final ObjectProperty<LocalDateTime> dateTimeValue = new SimpleObjectProperty<>(null);
  private final ObjectProperty<String> format = new SimpleObjectProperty<String>() {
    public void set(String newValue) {
      super.set(newValue);
      formatter = DateTimeFormatter.ofPattern(newValue);
    }
  };

  /**
   * method to align the columns
   */
  public void alignColumnCountWithFormat() {
    getEditor().setPrefColumnCount(getFormat().length());
  }

  /**
   * Constructor for setting up the datetimepicker
   */
  public DateTimePicker() {
    getStyleClass().add("datetime-picker");
    setFormat(DefaultFormat);
    setConverter(new InternalConverter());
    alignColumnCountWithFormat();

    // Syncronize changes to the underlying date value back to the dateTimeValue
    valueProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue == null) {
        dateTimeValue.set(null);
      } else {
        if (dateTimeValue.get() == null) {
          dateTimeValue.set(LocalDateTime.of(newValue, LocalTime.now()));
        } else if (!this.getEditor().getText().isEmpty()) {
          LocalTime time = LocalDateTime.parse(this.getEditor().getText(), formatter).toLocalTime();
          dateTimeValue.set(LocalDateTime.of(newValue, time));
        }
      }
    });

    // Syncronize changes to dateTimeValue back to the underlying date value
    dateTimeValue.addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        LocalDate dateValue = newValue.toLocalDate();
        boolean forceUpdate = dateValue.equals(valueProperty().get());
        // Make sure the display is updated even when the date itself wasn't changed
        setValue(dateValue);
        if (forceUpdate) setConverter(new InternalConverter());
      } else {
        setValue(null);
      }
    });

    // Persist changes onblur
    getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue) {
        simulateEnterPressed();
      }
    });

    // fixes bug in getting datetime values
    getEditor().setTextFormatter(new TextFormatter<>(getConverter()));

  }

  /**
   * simulate enter when bluring a field
   */
  private void simulateEnterPressed() {
    getEditor().commitValue();
  }

  /**
   * getter for getting the datetime value
   * @return LocalDateTime
   */
  public LocalDateTime getDateTimeValue() {
    return dateTimeValue.get();
  }

  /**
   * setter for the datetime value
   * @param dateTimeValue LocalDateTime
   */
  public void setDateTimeValue(LocalDateTime dateTimeValue) {
    this.dateTimeValue.set(dateTimeValue);
  }

  /**
   * getter for the object property of localdatetime
   * @return ObjectProperty
   */
  public ObjectProperty<LocalDateTime> dateTimeValueProperty() {
    return dateTimeValue;
  }

  /**
   * getter for format
   * @return String
   */
  public String getFormat() {
    return format.get();
  }

  /**
   * setter for format
   * @param format String
   */
  public void setFormat(String format) {
    this.format.set(format);
    alignColumnCountWithFormat();
  }

  /**
   * internal converter class for converting the datetime to a string
   */
  class InternalConverter extends StringConverter<LocalDate> {
    /**
     * classic toSttring on the localdate object
     * @param object LocalDate
     * @return String
     */
    public String toString(LocalDate object) {
      LocalDateTime value = getDateTimeValue();
      return (value != null) ? value.format(formatter) : "";
    }

    /**
     * Setter from a string to datetime
     * @param value string
     * @return datetime
     */
    public LocalDate fromString(String value) {
      if (value == null || value.isEmpty()) {
        dateTimeValue.set(null);
        return null;
      }
      dateTimeValue.set(LocalDateTime.parse(value, formatter));
      return dateTimeValue.get().toLocalDate();
    }
  }
}