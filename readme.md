# Byte Bank

## Overview
Byte Bank is a desktop application designed to simplify crypto wallet management. Built using JavaFX, the application features a robust and interactive user interface, threading for efficient performance, and a database for secure storage. This repository contains all the code and resources required to run, develop, and test Byte Bank.

## Features
- **Crypto Wallet Management**: Perform operations like viewing balances, transaction history, and initiating transactions.
- **JavaFX Frontend**: Sleek, modern UI for an intuitive user experience.
- **Threading**: Optimized for responsiveness and performance.
- **Database Integration**: Securely stores wallet data and transaction records.
- **JUnit Testing**: Includes comprehensive unit tests to ensure code quality.

## Getting Started

### Prerequisites
Ensure you have the following installed:
- Java Development Kit (JDK) 17
- Apache Maven 3.6+
- Git (for cloning the repository)

### Clone the Repository
```bash
$ git clone https://github.com/xrayian/byte_bank_aoop_java.git
$ cd byte_bank
```

### Build the Project
Run the following Maven command to build the project:
```bash
$ mvn clean install
```

### Run the Application
To run the application, execute:
```bash
$ mvn javafx:run
```

## Project Structure
```
byte_bank/
├── src/
│   ├── main/
│   │   ├── java/com/kernelcrash/byte_bank/  # Java source code
│   │   └── resources/com.kernelcrash/byte_bank/onboarding/  # FXML and assets
│   └── test/
│       └── java/com/kernelcrash/byte_bank/  # Unit tests
├── pom.xml  # Maven configuration file
└── README.md  # Project documentation
```

## Dependencies
The project uses the following dependencies:
- **JavaFX**: For building the user interface.
- **Ikonli**: For adding icons to the UI.
- **JUnit**: For testing purposes.

These dependencies are managed through Maven and specified in the `pom.xml` file.

## Development

### Adding New Features
1. Create a new branch for your feature:
   ```bash
   $ git checkout -b feature/<feature-name>
   ```
2. Implement the feature and commit your changes.
3. Push the branch and create a pull request for review.

### Running Tests
Execute the following to run unit tests:
```bash
$ mvn test
```

## Contribution Guidelines
- Follow the coding style and conventions used in the project.
- Ensure all changes are tested thoroughly.
- Update the documentation if new features are added.

## License
This project is private and proprietary. Unauthorized use, distribution, or modification is prohibited.

## Contact
For questions or support, please contact Rayian Mahi at [rbsmahi@gmail.com].

