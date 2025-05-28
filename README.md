# SwissSign Automation Assignment

## Overview

This project demonstrates automated API and UI testing for SwissSign services.

It includes:

- **API tests** using OkHttp and TestNG (with ExtentReports)
- **UI tests** using Selenium WebDriver and TestNG (with ExtentReports)
- Credentials are externalized in a properties file for safety

---

## Prerequisites

- **Java 21** (or higher)
- **Maven 3.8+**
- **Chrome browser** (for UI tests)
- **Internet connection** (for Maven and real API/UI calls)

---

## Installation

1.  **Clone the repository:**

    ```sh
    git clone https://github.com/aratai/swisssign.git
    cd swisssign-automation
    ```

2.  **Configure credentials:**

    - Copy `credentials.properties.example` to `credentials.properties` in `src/test/resources`:

    - Edit `credentials.properties` and fill in all properties shared with you thru other channels:

    ```properties
      email=
      password=
      username=
      userSecret=
    ```

---

## Running Tests

To execute all test run:

```sh
mvn clean test
```

## Test Reports

Reports are generated in the `/reports` directory, there should be two - one for UI tests and the other one for API tests.

Open the `.html` files in your browser to view detailed step-by-step results.

---

## How to Add New Tests

- Add new API tests in `src/test/java/api/`
- Add new UI tests and page objects in `src/test/java/ui/`
- Page object pattern is followed for UI automation

---

### Limitations & Further Improvements

- For a real-world framework, reporting, test data, and credentials should be fully decoupled.
- Tests can be made data-driven by adding TestNG DataProviders or parameterizing the credentials file.
- For advanced security, use environment variables or a secrets manager instead of flat files.

---

### Assignment Notes

- Assignment requirements were followed as closely as possible.
- If any real API endpoint or UI element differed from the instructions, the test logic was adjusted and noted in comments.
- Screenshots were omitted as per instructions to focus on reporting.
- Credentials are handled securely and not included in the repository.
- If given more time, the project would be further modularized and CI integration added.
- **Cross-browser testing** is not yet implemented but could be integrated via WebDriverManager or services like LambdaTest or BrowserStack.
- **Test stability** was a focus; explicit waits and error handling were used to mitigate flakiness, particularly with Shadow DOM.
- **Code style and readability** were prioritized by using the Page Object Model and splitting responsibilities between test logic and UI interactions.
- **Reports are unified per suite**, but could be enhanced further by combining UI and API into a single dashboard or aggregated report.

---
