# 📦 **SecureZip — Java File Encryption, Compression & Integrity Library**

[![Maven Central](https://img.shields.io/maven-central/v/io.github.sumitshresht/SecureZip.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.sumitshresht/SecureZip)
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)
![Java](https://img.shields.io/badge/Java-8+-blue.svg)
![JUnit](https://img.shields.io/badge/Tested-JUnit5-brightgreen)

---

## 🚀 **Overview**

**SecureZip** is a Java library that provides robust file encryption, compression, and integrity utilities. It supports AES encryption, standard and AES-encrypted ZIP compression, secure file deletion, file splitting/merging, and integrity checks (SHA-256, MD5).

---

## 🎯 **Features**

* 🔐 AES File Encryption & Decryption (Password-based)
* 📦 ZIP File Compression & Extraction
* 🔑 Password-Protected ZIP (Standard & AES-256)
* 🔍 List ZIP File Contents
* 🧠 File Integrity Check (SHA-256, SHA-1, MD5)
* ✂️ File Split and Merge (like WinRAR/7-Zip parts)
* 🔥 Secure File Delete (Wipe data before deletion)
* ✅ Fully Unit Tested with JUnit 5
* 💯 Easy to use, lightweight, no external dependencies

---

## 🔧 **Installation**

### ➤ **Maven:**

```xml
<dependency>
    <groupId>io.securezip</groupId>
    <artifactId>securezip</artifactId>
    <version>1.0.0</version>
</dependency>
```

### ➤ **Gradle:**

```gradle
dependencies {
    implementation 'io.securezip:securezip:1.0.0'
}
```


---

## 🚀 **Usage Examples**

### 🔐 **Encrypt and Decrypt a File**

```java
EncryptionUtils.encryptFile("/path/to/file.txt", "password");
EncryptionUtils.decryptFile("/path/to/file.txt.enc", "password");
```

---

### 📦 **Compress and Extract ZIP**

```java
CompressionUtils.compressFolder("/path/to/folder", "/path/to/output.zip");
CompressionUtils.extractZip("/path/to/output.zip", "/path/to/destination");
```

---

### 🔑 **Password Protected ZIP**

```java
CompressionUtils.createPasswordProtectedZip("/path/to/file", "/path/to/secure.zip", "password");
CompressionUtils.extractPasswordProtectedZip("/path/to/secure.zip", "password", "/path/to/extract");
```

---

### 🔍 **List ZIP Contents**

```java
CompressionUtils.listZipContents("/path/to/file.zip");
```

---

### 🧠 **Check File Integrity (Hash)**

```java
String sha256 = HashUtils.getSHA256("/path/to/file.txt");
String md5 = HashUtils.getMD5("/path/to/file.txt");
System.out.println("SHA-256: " + sha256);
```

---

### ✂️ **File Split and Merge**

```java
// Split file into 5MB parts
List<String> parts = FileSplitterUtils.splitFile("/path/to/largefile.zip", 5);

// Merge back
FileSplitterUtils.mergeFiles(parts, "/path/to/merged.zip");
```

---

### 🔥 **Secure File Delete (Wipe)**

```java
SecureDeleteUtils.secureDelete("/path/to/secretfile.txt");
```

---

## 🧪 **Running Tests**

* Right-click on `test/java` → Run All Tests
  or
* Use:

```bash
mvn test
```

or

```bash
gradle test
```

---

## 📁 **Project Structure**

```
SecureZip/
 ├── src/
 │   ├── main/java/io/securezip/...
 │   └── test/java/io/securezip/...
 ├── pom.xml
 ├── README.md
 └── LICENSE
```

---

## 📜 **License**

This project is licensed under the [MIT License](LICENSE).


---

## 🌟 **Contributions Welcome!**

If you have ideas, bug reports, or improvements — feel free to open issues or pull requests.

---

# 💥 **Star this repo ⭐ if you like it!**

---

