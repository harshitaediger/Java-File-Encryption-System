# Java File Encryption System

## About the Project
This project is developed using Java to protect files from unauthorized access. It uses password-based AES encryption to convert readable text into encrypted data. The original content can be restored by entering the correct password during decryption.

## Features
- Encrypts text files using a password.
- Decrypts files using the correct password.
- Uses AES-GCM encryption.
- Generates an encryption key using the password.
- Detects incorrect passwords or modified encrypted files.

## Technologies Used
- Java
- AES-GCM
- PBKDF2
- Java File Handling

## How to Run
1. Install Java JDK.
2. Open the project folder in VS Code.
3. Open the terminal.
4. Compile the program:

   `javac -d out Main.java`

5. Run the program:

   `java -cp out Main`

6. Select encryption or decryption.
7. Enter the input file path, output file path and password.

## Project Files
- `Main.java` – Java source code.
- `sample.txt` – Original text file.
- `encrypted.txt` – Encrypted output.
- `decrypted.txt` – Decrypted output.

## Note
Use the same password for encryption and decryption. Keep your password safe.
