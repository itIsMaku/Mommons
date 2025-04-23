<div align="center" style="position: relative;">
<img src="https://raw.githubusercontent.com/PKief/vscode-material-icon-theme/ec559a9f6bfd399b82bb44393651661b08aaf7ba/icons/folder-markdown-open.svg"  width="30%" style="margin: -20px 0 0 20px;">
<h1>MOMMONS</h1>
<p align="center">
	<em>Mommons: Modular Magic for Java Mastery!</em>
</p>
<p align="center">
	<img src="https://img.shields.io/github/license/itismaku/mommons?style=default&logo=opensourceinitiative&logoColor=white&color=0080ff" alt="license">
	<img src="https://img.shields.io/github/last-commit/itismaku/mommons?style=default&logo=git&logoColor=white&color=0080ff" alt="last-commit">
	<img src="https://img.shields.io/github/languages/top/itismaku/mommons?style=default&color=0080ff" alt="repo-top-language">
	<img src="https://img.shields.io/github/languages/count/itismaku/mommons?style=default&color=0080ff" alt="repo-language-count">
</p>
</div>
<br clear="right">

## 🔗 Table of Contents

- [📍 Overview](#-overview)
- [👾 Features](#-features)
- [📁 Project Structure](#-project-structure)
  - [📂 Project Index](#-project-index)
- [🚀 Getting Started](#-getting-started)
  - [☑️ Prerequisites](#-prerequisites)
  - [⚙️ Installation](#-installation)
  - [🤖 Usage](#🤖-usage)
  - [🧪 Testing](#🧪-testing)
- [🔰 Contributing](#-contributing)
- [🎗 License](#-license)

---

## 📍 Overview

The Mommons project is a modular Java-based software ecosystem designed to streamline development and deployment processes for distributed applications. Key features include consistent environment setup via Gradle and OpenJDK, efficient dependency management, and easy distribution of subprojects. It's ideal for developers working on complex server-side applications, particularly in environments requiring high scalability and maintainability.

---

## 👾 Features

|      | Feature         | Summary       |
| :--- | :---:           | :---          |
| ⚙️  | **Architecture**  | <ul><li>Utilizes Gradle for build management.</li><li>Structured into multiple subprojects (e.g., mommons-worker, mommons-spigot).</li><li>Java is the primary programming language.</li></ul> |
| 🔩 | **Code Quality**  | <ul><li>Includes specific YAML files for code quality checks.</li><li>Leverages Lombok for reducing boilerplate code in Java.</li><li>Utilizes GitHub Actions for continuous integration.</li></ul> |
| 📄 | **Documentation** | <ul><li>Documentation includes detailed setup and usage commands.</li><li>Java is the primary language with extensive use in the project.</li><li>Gradle commands are well-documented for building, running, and testing.</li></ul> |
| 🔌 | **Integrations**  | <ul><li>Integrates with Maven Central for dependency management.</li><li>Uses JitPack for publishing packages.</li><li>GitHub Actions for CI/CD processes.</li></ul> |
| 🧩 | **Modularity**    | <ul><li>Project is divided into multiple subprojects, each with its own build configuration.</li><li>Modular structure supports scalable development and maintenance.</li><li>Enables focused and independent development on subcomponents.</li></ul> |
| 🧪 | **Testing**       | <ul><li>Gradle is configured to handle unit testing.</li><li>Test configurations are managed through gradle.yml.</li><li>Supports automated testing via continuous integration.</li></ul> |
| ⚡️  | **Performance**   | <ul><li>Optimized Java compilation settings (UTF-8 encoding).</li><li>Efficient dependency management through Gradle.</li><li>Performance considerations likely addressed in individual subprojects.</li></ul> |
| 🛡️ | **Security**      | <ul><li>Uses secure dependencies from Maven Central.</li><li>Version control and environment consistency maintained through JitPack and GitHub Actions.</li><li>No explicit security tools or practices mentioned, focus on secure coding standards assumed.</li></ul> |
| 📦 | **Dependencies**  | <ul><li>Depends on external libraries like Lombok.</li><li>Gradle manages project dependencies across subprojects.</li><li>Dependencies are clearly defined in multiple build.gradle files.</li></ul> |
| 🚀 | **Scalability**   | <ul><li>Modular architecture supports scalability.</li><li>Gradle facilitates efficient management of large-scale projects.</li><li>CI/CD integrations aid in maintaining project scalability under continuous development.</li></ul> |
---

## 📁 Project Structure

```sh
└── mommons/
    ├── .github
    │   └── workflows
    ├── LICENSE
    ├── README.md
    ├── build.gradle
    ├── gradle
    │   └── wrapper
    ├── gradlew
    ├── gradlew.bat
    ├── jitpack.yml
    ├── mommons-bungee
    │   ├── build.gradle
    │   ├── settings.gradle
    │   └── src
    ├── mommons-entityframework
    │   ├── build.gradle
    │   ├── settings.gradle
    │   ├── src
    │   └── test.bat
    ├── mommons-shared
    │   ├── build.gradle
    │   ├── settings.gradle
    │   └── src
    ├── mommons-spigot
    │   ├── build.gradle
    │   ├── settings.gradle
    │   └── src
    ├── mommons-worker
    │   ├── build.gradle
    │   ├── settings.gradle
    │   └── src
    └── settings.gradle
```


### 📂 Project Index
<details open>
	<summary><b><code>MOMMONS/</code></b></summary>
	<details> <!-- __root__ Submodule -->
		<summary><b>__root__</b></summary>
		<blockquote>
			<table>
			<tr>
				<td><b><a href='https://github.com/itismaku/mommons/blob/master/gradlew.bat'>gradlew.bat</a></b></td>
				<td>- Gradlew.bat serves as the startup script for Gradle on Windows within the project, facilitating the initialization and configuration of the Gradle build environment<br>- It ensures the Java environment is correctly set, manages JVM options, and executes Gradle tasks, crucial for building and deploying the software efficiently.</td>
			</tr>
			<tr>
				<td><b><a href='https://github.com/itismaku/mommons/blob/master/jitpack.yml'>jitpack.yml</a></b></td>
				<td>- Specifies the Java development kit version and sets up the environment for the project by installing and configuring OpenJDK 17<br>- This configuration ensures that all developers and the continuous integration system use a consistent Java version, leading to more reliable builds and testing across different environments within the project's architecture.</td>
			</tr>
			<tr>
				<td><b><a href='https://github.com/itismaku/mommons/blob/master/build.gradle'>build.gradle</a></b></td>
				<td>- Configures the build environment for a Java-based project using Gradle, setting project metadata and specifying dependency management<br>- It establishes UTF-8 encoding for Java compilation, integrates Maven Central for repositories, and defines dependency on Lombok<br>- Additionally, it configures publishing settings for subprojects, facilitating their distribution via Maven with specified group ID, artifact ID, and versioning.</td>
			</tr>
			<tr>
				<td><b><a href='https://github.com/itismaku/mommons/blob/master/settings.gradle'>settings.gradle</a></b></td>
				<td>- Defines the overarching project structure for 'Mommons', organizing it into several subprojects: mommons-worker, mommons-spigot, mommons-bungee, mommons-shared, and mommons-entityframework<br>- Each subproject is assigned a specific directory, facilitating modular development and maintenance across different components of the Mommons software ecosystem.</td>
			</tr>
			</table>
		</blockquote>
	</details>
	<details> <!-- mommons-worker Submodule -->
		<summary><b>mommons-worker</b></summary>
		<blockquote>
			<table>
			<tr>
				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-worker/build.gradle'>build.gradle</a></b></td>
				<td>- Defines the build configuration for the 'mommons-worker' module within the 'cz.maku' project, specifying Java as the programming language and setting up Maven for publishing<br>- It manages dependencies critical for the module's functionality, including libraries for network communication, data processing, and annotation handling, ensuring compatibility and efficient project compilation.</td>
			</tr>
			<tr>
				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-worker/settings.gradle'>settings.gradle</a></b></td>
				<td>- Defines the root project name as "mommons-worker" and integrates the "mommons-shared" module by setting its directory path relative to the current project<br>- This configuration facilitates modular development by linking shared resources and functionalities, enhancing maintainability and scalability within the broader architecture of the codebase.</td>
			</tr>
			</table>
			<details>
				<summary><b>src</b></summary>
				<blockquote>
					<details>
						<summary><b>main</b></summary>
						<blockquote>
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<details>
										<summary><b>cz</b></summary>
										<blockquote>
											<details>
												<summary><b>maku</b></summary>
												<blockquote>
													<details>
														<summary><b>mommons</b></summary>
														<blockquote>
															<details>
																<summary><b>worker</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-worker/src/main/java/cz/maku/mommons/worker/WorkerExecutable.java'>WorkerExecutable.java</a></b></td>
																		<td>- WorkerExecutable in the Mommons project dynamically handles method and constructor invocations within worker classes, managing execution based on annotations that define task repetition, asynchronous behavior, and initialization sequences<br>- It ensures parameter compatibility and leverages logging for error handling, enhancing the robustness and flexibility of task execution in the system.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-worker/src/main/java/cz/maku/mommons/worker/Worker.java'>Worker.java</a></b></td>
																		<td>- Worker.java serves as the core component for managing and initializing services within the Mommons project<br>- It dynamically registers, initializes, and terminates services annotated with @Service, handling dependencies and lifecycle events<br>- The class also integrates logging and database connectivity, ensuring robust service management and error handling.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-worker/src/main/java/cz/maku/mommons/worker/WorkerServiceClass.java'>WorkerServiceClass.java</a></b></td>
																		<td>- WorkerServiceClass orchestrates the initialization, execution, and lifecycle management of worker services within the Mommons project<br>- It handles field injections, method invocations, and scheduled tasks, ensuring that services are dynamically loaded, executed, and periodically operated based on annotations<br>- This class also manages error logging and resource cleanup.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-worker/src/main/java/cz/maku/mommons/worker/WorkerField.java'>WorkerField.java</a></b></td>
																		<td>- WorkerField in the Mommons project encapsulates functionality for managing field values within worker objects dynamically<br>- It supports checking for specific annotations and safely updating field values, ensuring fields are accessible before modification<br>- This class is crucial for reflection-based operations where fields in worker objects need to be manipulated at runtime based on their annotations.</td>
																	</tr>
																	</table>
																	<details>
																		<summary><b>annotation</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-worker/src/main/java/cz/maku/mommons/worker/annotation/Initialize.java'>Initialize.java</a></b></td>
																				<td>- Defines an annotation named `Initialize` used within the `mommons-worker` module to designate specific methods for initialization tasks<br>- Applied at runtime, this annotation targets methods, signaling to the framework or application when and where initial setup procedures are necessary, enhancing modularity and clarity in method functionality across the system.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-worker/src/main/java/cz/maku/mommons/worker/annotation/Load.java'>Load.java</a></b></td>
																				<td>- Defines a custom annotation `Load` used within the Mommons Worker module to mark fields and parameters that require dynamic content injection<br>- This annotation facilitates the automatic handling of dependencies in the application, streamlining the initialization and configuration processes across the system's architecture.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-worker/src/main/java/cz/maku/mommons/worker/annotation/PostInitialize.java'>PostInitialize.java</a></b></td>
																				<td>- Defines a custom annotation, `PostInitialize`, used within the Mommons Worker module to designate methods that should be executed after the initialization phase of a component<br>- This annotation targets methods specifically, allowing for automated post-setup operations crucial for preparing components for use within the broader application architecture.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-worker/src/main/java/cz/maku/mommons/worker/annotation/Destroy.java'>Destroy.java</a></b></td>
																				<td>- Defines a `Destroy` annotation used within the Mommons Worker module to specify methods that handle cleanup or teardown processes post-execution<br>- It includes an optional delay attribute, allowing developers to define a waiting period before the annotated method is executed, enhancing the flexibility and control over resource management in the application lifecycle.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-worker/src/main/java/cz/maku/mommons/worker/annotation/Service.java'>Service.java</a></b></td>
																				<td>- Defines a custom Java annotation `Service` used to mark classes within the Mommons Worker module<br>- It categorizes services based on their functionalities such as command handling, event listening, scheduled tasks, conditional operations, and SQL interactions<br>- This annotation helps in managing service behavior and feature activation across the application's architecture.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-worker/src/main/java/cz/maku/mommons/worker/annotation/Async.java'>Async.java</a></b></td>
																				<td>- Defines a custom Java annotation named `Async` used to mark methods within the `mommons-worker` module<br>- This annotation indicates that the marked methods should be executed asynchronously, supporting concurrent processing in the application<br>- It applies at runtime and targets method-level implementation, enhancing the module's ability to handle tasks efficiently without blocking the main execution flow.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-worker/src/main/java/cz/maku/mommons/worker/annotation/Repeat.java'>Repeat.java</a></b></td>
																				<td>- Defines a custom Java annotation `Repeat` used to specify repetition behavior for methods within the Mommons Worker module<br>- It allows configuration of the execution period and initial delay, facilitating scheduled operations across the application<br>- This annotation plays a crucial role in automating tasks at defined intervals within the service architecture.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-worker/src/main/java/cz/maku/mommons/worker/annotation/AnotherThread.java'>AnotherThread.java</a></b></td>
																				<td>- Defines a custom Java annotation, `AnotherThread`, used to mark methods within the `mommons-worker` module that should execute on a separate thread<br>- This annotation facilitates concurrent processing, enhancing the application's performance by allowing certain operations to run independently of the main execution flow.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-worker/src/main/java/cz/maku/mommons/worker/annotation/Condition.java'>Condition.java</a></b></td>
																				<td>- Defines a runtime annotation `Condition` applicable to types, which specifies a boolean condition<br>- This annotation is crucial for conditional logic in the application, enabling dynamic behavior based on the specified conditions<br>- It plays a significant role in controlling the execution flow within the `mommons-worker` module of the codebase.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-worker/src/main/java/cz/maku/mommons/worker/annotation/Plugin.java'>Plugin.java</a></b></td>
																				<td>- Defines an annotation for marking classes as plugins within the Mommons Worker module<br>- The annotation includes metadata such as plugin name, description, main class, authors, version, API version, dependencies, and website<br>- This facilitates the management and integration of various plugins in the system, enhancing modularity and scalability.</td>
																			</tr>
																			</table>
																			<details>
																				<summary><b>sql</b></summary>
																				<blockquote>
																					<table>
																					<tr>
																						<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-worker/src/main/java/cz/maku/mommons/worker/annotation/sql/Download.java'>Download.java</a></b></td>
																						<td>- Defines an annotation used within the Mommons Worker module to specify SQL download operations<br>- The annotation targets methods, allowing developers to declare the SQL table, query, and scheduling parameters such as period and delay for automated data retrieval tasks within the application's architecture.</td>
																					</tr>
																					</table>
																				</blockquote>
																			</details>
																		</blockquote>
																	</details>
																</blockquote>
															</details>
															<details>
																<summary><b>subscriber</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-worker/src/main/java/cz/maku/mommons/subscriber/Subscriber.java'>Subscriber.java</a></b></td>
																		<td>- Defines the Subscriber interface within the Mommons project, crucial for managing subscription mechanisms<br>- It allows for the registration and unregistration of subscribers, retrieval of subscriber details, and the distribution of messages to specific subscribers<br>- This interface supports robust and dynamic subscriber management across the system, facilitating effective communication and data flow.</td>
																	</tr>
																	</table>
																	<details>
																		<summary><b>local</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-worker/src/main/java/cz/maku/mommons/subscriber/local/LocalSubscriber.java'>LocalSubscriber.java</a></b></td>
																				<td>- Defines an interface, LocalSubscriber, within the Mommons project, specifically for handling local message subscriptions<br>- It outlines a method for receiving messages, enabling components to react to data or events distributed locally within the application<br>- This interface is crucial for facilitating internal communication and event-driven interactions among different parts of the system.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-worker/src/main/java/cz/maku/mommons/subscriber/local/LocalSubscribersService.java'>LocalSubscribersService.java</a></b></td>
																				<td>- LocalSubscribersService manages a collection of local subscribers within the Mommons project<br>- It initializes, registers, and unregisters subscribers, maintaining a concurrent map for thread-safe operations<br>- The service also facilitates the targeted delivery of messages to registered subscribers, ensuring efficient and selective communication across the system.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																</blockquote>
															</details>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
				</blockquote>
			</details>
		</blockquote>
	</details>
	<details> <!-- .github Submodule -->
		<summary><b>.github</b></summary>
		<blockquote>
			<details>
				<summary><b>workflows</b></summary>
				<blockquote>
					<table>
					<tr>
						<td><b><a href='https://github.com/itismaku/mommons/blob/master/.github/workflows/gradle.yml'>gradle.yml</a></b></td>
						<td>- Automates the continuous integration process for a Java project using GitHub Actions, triggered by code pushes<br>- It sets up Java, makes the Gradle wrapper executable, validates it, builds JAR files for different modules, and uploads these artifacts, ensuring each component is correctly compiled and stored for further use or deployment.</td>
					</tr>
					<tr>
						<td><b><a href='https://github.com/itismaku/mommons/blob/master/.github/workflows/code_quality.yml'>code_quality.yml</a></b></td>
						<td>- Qodana, a code quality tool, is integrated into the project's workflow to analyze code during pull requests and pushes to the '2.0' branch<br>- Running on Ubuntu, it checks out the full commit history for accurate analysis and utilizes a secure token for authentication, ensuring high standards of code quality and security compliance across the development lifecycle.</td>
					</tr>
					</table>
				</blockquote>
			</details>
		</blockquote>
	</details>
	<details> <!-- mommons-spigot Submodule -->
		<summary><b>mommons-spigot</b></summary>
		<blockquote>
			<table>
			<tr>
				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/build.gradle'>build.gradle</a></b></td>
				<td>- Configures the build environment for the Mommons-Spigot module, specifying Java as the programming language and setting up Maven for dependency management<br>- It integrates various libraries and project modules essential for the development, including Spigot API and additional utilities like Guava and Gson, to enhance functionality related to Minecraft server operations.</td>
			</tr>
			<tr>
				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/settings.gradle'>settings.gradle</a></b></td>
				<td>Defines the structure and interdependencies of the "mommons-spigot" project, setting it as the root and incorporating three subprojects: "mommons-worker," "mommons-shared," and "mommons-entityframework." Each subproject is linked to its respective directory, establishing a clear modular architecture that supports separate development and maintenance paths within the broader project ecosystem.</td>
			</tr>
			</table>
			<details>
				<summary><b>src</b></summary>
				<blockquote>
					<details>
						<summary><b>main</b></summary>
						<blockquote>
							<details>
								<summary><b>resources</b></summary>
								<blockquote>
									<table>
									<tr>
										<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/resources/plugin.yml'>plugin.yml</a></b></td>
										<td>- MommonsLoader, defined in the plugin.yml, serves as the central loader for all plugins that depend on the Mommons framework<br>- It specifies MommonsPlugin as the main class, authored by itIsMaku, and outlines dependencies like HolographicDisplays<br>- This configuration ensures compatibility with API version 1.17 and enhances the modular architecture by managing inter-plugin dependencies and version control.</td>
									</tr>
									</table>
								</blockquote>
							</details>
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<details>
										<summary><b>cz</b></summary>
										<blockquote>
											<details>
												<summary><b>maku</b></summary>
												<blockquote>
													<details>
														<summary><b>mommons</b></summary>
														<blockquote>
															<details>
																<summary><b>worker</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/worker/BukkitWorkerMethod.java'>BukkitWorkerMethod.java</a></b></td>
																		<td>- BukkitWorkerMethod class in the Mommons-Spigot project extends WorkerExecutable to handle Minecraft server tasks<br>- It determines whether methods are designated as commands or events by checking for specific annotations<br>- This functionality integrates with the server's command and event handling system, enhancing the modular architecture of the codebase by allowing dynamic method classification and execution.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/worker/WorkerReceiver.java'>WorkerReceiver.java</a></b></td>
																		<td>- WorkerReceiver in the Mommons project facilitates interaction with worker plugins by providing methods to retrieve specific worker instances and services<br>- It enables access to both plugin-specific and core workers, along with their associated services and configurations, streamlining the integration and management of plugin functionalities within the broader system architecture.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/worker/WorkerBukkitServiceClass.java'>WorkerBukkitServiceClass.java</a></b></td>
																		<td>- WorkerBukkitServiceClass extends functionality within a Bukkit-based Java plugin, managing asynchronous and synchronous tasks, SQL data handling, and dynamic command and event registration<br>- It integrates with the broader system to facilitate scheduled operations, command execution, and event handling, enhancing modularity and scalability of the plugin's architecture.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/worker/BukkitWorker.java'>BukkitWorker.java</a></b></td>
																		<td>- BukkitWorker serves as a crucial component within the Mommons project, managing the lifecycle and integration of services within a Bukkit (Minecraft server) environment<br>- It initializes, logs, and cleanly shuts down services, ensuring that each component is correctly set up and tied to the server's plugin system for optimal operation and resource management.</td>
																	</tr>
																	</table>
																	<details>
																		<summary><b>plugin</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/worker/plugin/WorkerPlugin.java'>WorkerPlugin.java</a></b></td>
																				<td>- WorkerPlugin.java serves as an abstract base for plugins within the Mommons project, managing the lifecycle and integration of BukkitWorker instances<br>- It orchestrates the initialization, loading, and unloading of services, while also handling plugin enablement and disablement processes<br>- This class ensures seamless communication and service management across the plugin's architecture.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>annotation</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/worker/annotation/BukkitCommand.java'>BukkitCommand.java</a></b></td>
																				<td>- BukkitCommand serves as a custom annotation within the Mommons Spigot project, enabling developers to define new commands in Minecraft servers<br>- It specifies command properties such as name, description, usage, aliases, and a fallback prefix, streamlining the integration and management of commands across the server's architecture.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/worker/annotation/BukkitEvent.java'>BukkitEvent.java</a></b></td>
																				<td>- Defines an annotation used within the Mommons Spigot project to mark methods that handle Bukkit events<br>- By specifying the event type and priority, it streamlines the integration of event handling in Minecraft server plugins, enhancing modularity and maintainability of the codebase<br>- This annotation facilitates efficient event management and customization in plugin development.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																</blockquote>
															</details>
															<details>
																<summary><b>player</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/player/CloudPlayer.java'>CloudPlayer.java</a></b></td>
																		<td>- CloudPlayer in the Mommons project manages player data interactions, facilitating both local and cloud-based storage operations<br>- It supports player data retrieval, updates, and synchronization across different servers, leveraging asynchronous operations to enhance performance and scalability within the multiplayer server environment<br>- Additionally, it provides functionalities for player server connectivity.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/player/PlayerBukkitService.java'>PlayerBukkitService.java</a></b></td>
																		<td>- PlayerBukkitService in the Mommons-Spigot project manages player data interactions within a Minecraft server environment<br>- It handles player events such as pre-login, join, and quit by initializing, loading, and unloading player data respectively, ensuring data consistency and triggering custom events for further processing within the system's architecture.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/player/PlayerDataRepository.java'>PlayerDataRepository.java</a></b></td>
																		<td>- PlayerDataRepository manages player data interactions within the Mommons project, specifically handling the initialization, loading, and asynchronous database operations for player instances<br>- It ensures player data consistency across sessions by interfacing with the server's data services and a MySQL database, and also manages player events and errors related to data retrieval or storage.</td>
																	</tr>
																	</table>
																	<details>
																		<summary><b>event</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/player/event/CloudPlayerUnloadEvent.java'>CloudPlayerUnloadEvent.java</a></b></td>
																				<td>- CloudPlayerUnloadEvent in the mommons-spigot project triggers when a player is unloaded from the server<br>- It extends the PlayerEvent class, utilizing the Bukkit API to manage player events efficiently<br>- This class is crucial for handling player-specific actions upon disconnection, ensuring resources are appropriately freed and maintaining server performance.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/player/event/CloudPlayerLoadEvent.java'>CloudPlayerLoadEvent.java</a></b></td>
																				<td>- CloudPlayerLoadEvent in the mommons-spigot project triggers when a CloudPlayer instance is loaded, integrating with the Minecraft server events<br>- It extends PlayerEvent, allowing developers to handle custom behaviors linked to player data management<br>- Features include event cancellation and access to the associated CloudPlayer, enhancing control over player interactions within the game environment.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/player/event/CloudPlayerPreUnloadEvent.java'>CloudPlayerPreUnloadEvent.java</a></b></td>
																				<td>- CloudPlayerPreUnloadEvent in the mommons-spigot project triggers before a CloudPlayer instance is unloaded, facilitating cleanup or data saving actions<br>- It extends PlayerEvent, integrating with Bukkit's event system to ensure smooth multiplayer interactions<br>- The event encapsulates both the standard Player and the specialized CloudPlayer, allowing for detailed context during event handling.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																</blockquote>
															</details>
															<details>
																<summary><b>token</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/token/NetworkTokenAction.java'>NetworkTokenAction.java</a></b></td>
																		<td>- NetworkTokenAction.java defines a class responsible for managing actions associated with network tokens in the Mommons Spigot project<br>- It encapsulates token-related data, including the token itself, associated actions, target server details, and expiration specifics, facilitating secure and timed interactions across different servers within the network.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/token/NetworkTokenWorker.java'>NetworkTokenWorker.java</a></b></td>
																		<td>- NetworkTokenWorker manages the asynchronous retrieval and processing of network tokens from a database, specifically targeting unexecuted tokens linked to a server<br>- It checks token validity, logs errors for expired or unactionable tokens, and triggers registered actions if valid, updating execution status post-processing.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/token/NetworkTokenService.java'>NetworkTokenService.java</a></b></td>
																		<td>- NetworkTokenService in the Mommons project manages the distribution and tracking of tokens across different servers<br>- It facilitates the addition of token-related actions, and supports asynchronous and synchronous token transmission with expiration settings, ensuring secure and efficient inter-server communication.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/token/Token.java'>Token.java</a></b></td>
																		<td>- Token.java serves as a utility within the Mommons Spigot project, facilitating the creation of secure, randomized tokens<br>- Each token is associated with a specific action and carries a set of data<br>- This functionality is crucial for managing actions and ensuring secure data handling across the system's various components.</td>
																	</tr>
																	</table>
																</blockquote>
															</details>
															<details>
																<summary><b>plugin</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/plugin/MommonsPlugin.java'>MommonsPlugin.java</a></b></td>
																		<td>- MommonsPlugin serves as the core component of the Mommons project, initializing and managing essential services such as database connections, data tables, and plugin dependencies<br>- It configures SQL tables for various data types, handles plugin loading and unloading processes, and integrates with external plugins to enhance functionality.</td>
																	</tr>
																	</table>
																</blockquote>
															</details>
															<details>
																<summary><b>bukkit</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/Inventories.java'>Inventories.java</a></b></td>
																		<td>- Inventories.java provides utility functions for managing inventory slots within the Bukkit framework, specifically for identifying and counting empty slots in a given inventory<br>- It enhances inventory management capabilities, crucial for game modifications involving item storage and handling in the Minecraft server environment managed by the mommons-spigot project.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/Items.java'>Items.java</a></b></td>
																		<td>- Items.java serves as a utility class within the Mommons Spigot project, facilitating the creation and manipulation of various item types in Minecraft<br>- It provides methods to generate custom items, books, and player heads with specific attributes, enhancing gameplay by allowing for personalized item features and interactions.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/Bukkits.java'>Bukkits.java</a></b></td>
																		<td>- Bukkits.java facilitates the conversion between Location objects and their string representations within the Mommons Spigot project<br>- It provides methods to serialize a Location to a string format and deserialize it back, using a defined separator<br>- This utility supports spatial data handling across different components of the Bukkit-based system.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/TextComponents.java'>TextComponents.java</a></b></td>
																		<td>- TextComponents in the mommons-spigot project facilitates the creation and dispatch of stylized chat messages within a Minecraft server environment<br>- It enables the embedding of interactive elements like hover and click events into messages, enhancing user interaction by allowing custom actions and tooltips directly in the chat interface.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/BossBars.java'>BossBars.java</a></b></td>
																		<td>- BossBars.java in the Mommons-Spigot project facilitates the creation of customizable boss bars in Minecraft<br>- It leverages the Bukkit API to generate bars with specified text, color, style, and namespace, enhancing the visual feedback and interaction within the game environment<br>- This component is integral for dynamic in-game notifications and status displays.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/Colors.java'>Colors.java</a></b></td>
																		<td>- Colors.java provides utility functions for handling text colorization and formatting within the Bukkit-based Minecraft server environment<br>- It enables the translation of color codes, formatting clickable text elements, and the removal of color codes from strings, enhancing the visual presentation and interaction of text in the game.</td>
																	</tr>
																	</table>
																	<details>
																		<summary><b>hologram</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/hologram/Lines.java'>Lines.java</a></b></td>
																				<td>- Defines an interface crucial for managing holographic display lines within the Mommons Spigot project<br>- Specifically, it facilitates the retrieval of hologram lines, which are essential for dynamic visual content in Minecraft servers<br>- This interface supports the modular architecture of the project by allowing various implementations that can handle different types of holographic content.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/hologram/TextHologramLine.java'>TextHologramLine.java</a></b></td>
																				<td>- TextHologramLine, part of the Mommons Spigot project, extends the functionality of holographic displays by managing text-based hologram lines<br>- It integrates with the HolographicDisplays API to facilitate the creation and manipulation of text within holograms, enhancing visual elements in the server environment<br>- This class specifically handles the storage and retrieval of text for holographic lines.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/hologram/Holograms.java'>Holograms.java</a></b></td>
																				<td>- Holograms.java serves as a central management module for creating and handling holographic displays within the Mommons Spigot project<br>- It facilitates the creation of holograms with dynamic content, manages a registry of these holograms, and supports the integration of event listeners to respond to changes in hologram line data.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/hologram/Hologram.java'>Hologram.java</a></b></td>
																				<td>- Hologram.java serves as a core component for managing and manipulating holographic displays within the MommonsPlugin environment<br>- It facilitates the creation, modification, and interaction of holograms, supporting dynamic content through animated text and item integration, and provides visibility control specific to player interactions in a Minecraft server setting.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/hologram/LinesChangeListener.java'>LinesChangeListener.java</a></b></td>
																				<td>- LinesChangeListener.java defines an interface crucial for monitoring changes in hologram text within the Mommons Spigot project<br>- It enables other components of the system to react dynamically when the content of a hologram is updated, ensuring that all visual representations are current and accurate, thereby enhancing the interactive experience in the Minecraft server environment.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/hologram/AnimatedTextHologramLine.java'>AnimatedTextHologramLine.java</a></b></td>
																				<td>- AnimatedTextHologramLine serves as an abstract base for creating animated text lines within holograms in the Mommons Spigot project<br>- It extends TextHologramLine, incorporating a refresh rate to manage animation timing, and requires implementation of a method to define behavior at each refresh interval, facilitating dynamic visual content in server environments.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/hologram/ItemHologramLine.java'>ItemHologramLine.java</a></b></td>
																				<td>- ItemHologramLine.java defines a specialized hologram line for displaying item stacks within the Mommons Spigot project<br>- It extends the generic HologramLine class, integrating with the HolographicDisplays API to manage and render Minecraft item visuals as part of holographic displays, enhancing interactive elements in the game environment.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/hologram/HologramLine.java'>HologramLine.java</a></b></td>
																				<td>- HologramLine.java serves as an abstract base for creating various types of holographic display lines within the Mommons Spigot project<br>- It encapsulates the functionality necessary to interact with the HolographicDisplays API, specifically managing hologram lines<br>- This abstraction facilitates the extension and customization of hologram content, crucial for dynamic in-game displays.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>armorstand</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/armorstand/ArmorStandContent.java'>ArmorStandContent.java</a></b></td>
																				<td>- ArmorStandContent in the mommons-spigot project encapsulates the equipment an armor stand can display in a Minecraft server environment<br>- It defines storage for armor pieces and items, including headgear, chestplate, leggings, boots, and items held in both hands, facilitating customization and manipulation of armor stand appearances within the game.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/armorstand/ArmorStand.java'>ArmorStand.java</a></b></td>
																				<td>- ArmorStand.java defines an abstract class within the Mommons Spigot project, central to managing armor stand entities in a Minecraft server environment<br>- It integrates with holographic displays and provides a framework for custom player interactions with these entities, encapsulating properties like identity, location, and content specifics.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/armorstand/ArmorStands.java'>ArmorStands.java</a></b></td>
																				<td>- ArmorStands.java facilitates the creation and management of customizable armor stands in a Minecraft server environment<br>- It integrates holographic text features above the stands, allowing dynamic visual customization<br>- The class handles the positioning and equipping of armor stands with various items, enhancing interactive game elements.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/armorstand/ArmorStandsBukkitService.java'>ArmorStandsBukkitService.java</a></b></td>
																				<td>- ArmorStandsBukkitService in the Mommons-Spigot project serves as a listener for hologram line changes within the Bukkit framework<br>- It registers itself to monitor and react to modifications in hologram displays, ensuring dynamic updates and interactions in the game environment<br>- This component enhances the user experience by providing real-time hologram adjustments.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>scheduler</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/scheduler/Schedulers.java'>Schedulers.java</a></b></td>
																				<td>- Schedulers.java serves as a utility class within the Mommons Bukkit plugin, providing methods to manage and schedule tasks on both synchronous and asynchronous bases<br>- It leverages the BukkitScheduler for timing operations, facilitating repeated and delayed task execution, crucial for handling game events and plugin functionality efficiently.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>menu</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/menu/Item.java'>Item.java</a></b></td>
																				<td>- Item.java serves as a utility class within the Mommons Spigot project, facilitating the creation and manipulation of item entities in a Minecraft server environment<br>- It provides methods to configure item properties such as material type, name, lore, enchantments, and visibility flags, streamlining the customization of items for gameplay enhancements.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/menu/Menu.java'>Menu.java</a></b></td>
																				<td>- Menu.java serves as a foundational component in the Mommons Spigot project, managing interactive player menus within a Minecraft server environment<br>- It facilitates the creation, display, and interaction handling of custom inventory interfaces, allowing for dynamic element management and event-driven actions specific to player interactions.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/menu/MenuElement.java'>MenuElement.java</a></b></td>
																				<td>- MenuElement in the Mommons Spigot project serves as a fundamental component for creating interactive menu items within the game's user interface<br>- It encapsulates both the visual representation of an item and its associated behavior, which is triggered by player interactions, specifically inventory click events<br>- This class streamlines the development of customizable, interactive menus for players.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/menu/MenuManager.java'>MenuManager.java</a></b></td>
																				<td>- MenuManager in the mommons-spigot project serves as a central hub for managing player-specific menu interactions within a Minecraft server environment<br>- It tracks and handles menu states for players using a UUID-keyed map, facilitating menu operations such as opening, clicking, and closing through event-driven methods, thereby enhancing the interactive experience in the game's UI.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/menu/MenuContainer.java'>MenuContainer.java</a></b></td>
																				<td>- MenuContainer.java serves as a management layer for user interface elements within a Bukkit-based Minecraft server plugin<br>- It facilitates the dynamic organization and manipulation of menu elements and sub-containers, ensuring elements are correctly positioned within predefined slots and handling exceptions when elements exceed container bounds.</td>
																			</tr>
																			</table>
																			<details>
																				<summary><b>exception</b></summary>
																				<blockquote>
																					<table>
																					<tr>
																						<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/bukkit/menu/exception/MenuContainerSizeException.java'>MenuContainerSizeException.java</a></b></td>
																						<td>- Defines a custom exception, MenuContainerSizeException, used within the Mommons Spigot project to handle errors specifically related to incorrect menu container sizes in the Bukkit menu system<br>- This exception aids in enforcing correct menu dimensions, ensuring that the user interface behaves as expected across the application.</td>
																					</tr>
																					</table>
																				</blockquote>
																			</details>
																		</blockquote>
																	</details>
																</blockquote>
															</details>
															<details>
																<summary><b>dependency</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/dependency/Dependencies.java'>Dependencies.java</a></b></td>
																		<td>- Dependencies.java manages the loading and initialization of plugin dependencies within the Mommons Spigot framework<br>- It dynamically resolves dependencies, handles their lifecycle, and ensures isolation where necessary, enhancing modularity and error handling in plugin development<br>- The class supports conditional loading and isolated execution environments for robust plugin functionality.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/dependency/Dependency.java'>Dependency.java</a></b></td>
																		<td>- Dependency.java serves as an abstract base for creating and managing dependencies within the Mommons Spigot plugin architecture<br>- It encapsulates the lifecycle of dependencies, including their construction, initialization, and potential reconstruction, ensuring that each dependency is properly integrated and functional within the broader plugin ecosystem.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/dependency/DependencyClassProvider.java'>DependencyClassProvider.java</a></b></td>
																		<td>- DependencyClassProvider serves as an interface within the Mommons Spigot project, defining methods to manage and verify the loading of dependencies for plugins<br>- It ensures that classes representing dependencies are correctly identified and assesses their compatibility with the core application, facilitating smooth integration and functionality enhancement in the broader server architecture.</td>
																	</tr>
																	</table>
																	<details>
																		<summary><b>isolation</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/dependency/isolation/IsolatedEnvironment.java'>IsolatedEnvironment.java</a></b></td>
																				<td>- IsolatedEnvironment.java defines an interface crucial for creating isolated execution environments within the Mommons Spigot project<br>- It enables the safe execution of operations with dependencies that should not interact with the main application context, thereby enhancing modularity and reducing conflict among components across the software architecture.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/dependency/isolation/IsolatedEnvironmentProvider.java'>IsolatedEnvironmentProvider.java</a></b></td>
																				<td>- IsolatedEnvironmentProvider serves as an abstract base within the Mommons Spigot project, facilitating the creation of isolated runtime environments for plugins<br>- It determines the specific environment class for isolation and checks if a plugin can run based on its dependencies, ensuring compatibility and stability across different plugin executions.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																</blockquote>
															</details>
															<details>
																<summary><b>server</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/server/ServerBukkitService.java'>ServerBukkitService.java</a></b></td>
																		<td>- ServerBukkitService in the Mommons-Spigot project manages player interactions with the server, handling events such as pre-login checks, player joins, and quits to ensure database connectivity and accurate player count<br>- It also processes specific commands, allowing privileged users to modify player permissions dynamically.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/server/ServerData.java'>ServerData.java</a></b></td>
																		<td>- ServerData.java serves as a utility class within the Mommons Spigot project, providing methods to access and filter server data<br>- It enables retrieval of server details by ID, type, or custom conditions, leveraging immutable data structures to ensure thread-safe operations and maintain data integrity across the application.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/server/LocalServerInfo.java'>LocalServerInfo.java</a></b></td>
																		<td>- LocalServerInfo serves as a utility class within the Mommons Spigot project, primarily responsible for managing and retrieving the local server's IP address and port information<br>- It facilitates asynchronous acquisition of server details and integrates with the WorkerReceiver to fetch server configurations dynamically, enhancing the system's adaptability and responsiveness in network-related operations.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/server/Server.java'>Server.java</a></b></td>
																		<td>- Server.java defines a Server class that manages server-specific data, integrating both local and cloud storage mechanisms<br>- It supports operations like retrieving and updating server details, player counts, and server types, leveraging asynchronous communication for data handling<br>- The class also ensures data persistence through MySQL and local storage, facilitating efficient data synchronization and retrieval.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/server/ServerDataService.java'>ServerDataService.java</a></b></td>
																		<td>- ServerDataService in the Mommons project initializes and manages server data, interfacing with a MySQL database to retrieve and store server-specific configurations and information<br>- It ensures data consistency by updating or inserting new server details during initialization and removing them upon destruction.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/server/ServerDataRepository.java'>ServerDataRepository.java</a></b></td>
																		<td>- ServerDataRepository manages the synchronization and updating of server data within the Mommons project<br>- It periodically fetches server details from a database, updates a concurrent map with fresh server information, and ensures outdated entries are removed<br>- This component enhances data consistency and availability across the system by leveraging asynchronous operations.</td>
																	</tr>
																	</table>
																</blockquote>
															</details>
															<details>
																<summary><b>cloud</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/cloud/DirectCloud.java'>DirectCloud.java</a></b></td>
																		<td>- DirectCloud in the Mommons-Spigot project facilitates interaction with cloud storage specific to servers and players, providing methods to insert, update, and retrieve data<br>- It utilizes MySQL for database operations and is marked for future removal, indicating a shift towards newer methodologies or technologies within the project's architecture.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/cloud/DirectCloudStorage.java'>DirectCloudStorage.java</a></b></td>
																		<td>- DirectCloudStorage serves as an enumeration within the Mommons project, defining specific cloud storage categories for servers and players<br>- It maps these categories to corresponding SQL tables, facilitating data management and retrieval<br>- Marked for deprecation, this component highlights planned updates and refactoring in the project's approach to handling cloud-based data storage.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/cloud/CloudData.java'>CloudData.java</a></b></td>
																		<td>- CloudData in the mommons-spigot project serves as an interface for interacting with cloud-based data storage<br>- It allows retrieval and asynchronous updating of values associated with specific keys<br>- Marked as deprecated and scheduled for removal, it suggests a shift towards newer methods or architectures within the project for handling cloud data operations.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-spigot/src/main/java/cz/maku/mommons/cloud/CachedCloud.java'>CachedCloud.java</a></b></td>
																		<td>- CachedCloud serves as a caching layer within the Mommons project, managing asynchronous data interactions with a MySQL database<br>- It updates and retrieves key-value pairs, ensuring data consistency and reducing database query load by maintaining a local cache that is periodically synchronized with the database.</td>
																	</tr>
																	</table>
																</blockquote>
															</details>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
				</blockquote>
			</details>
		</blockquote>
	</details>
	<details> <!-- mommons-bungee Submodule -->
		<summary><b>mommons-bungee</b></summary>
		<blockquote>
			<table>
			<tr>
				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-bungee/build.gradle'>build.gradle</a></b></td>
				<td>- Configures the build environment for the 'mommons-bungee' module within the larger project, specifying Java as the programming language and setting up Maven for dependencies management<br>- It integrates essential libraries and projects, ensuring compatibility and enhancing functionality with external Maven repositories for comprehensive software development and distribution.</td>
			</tr>
			<tr>
				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-bungee/settings.gradle'>settings.gradle</a></b></td>
				<td>- Defines the structure and interdependencies of the "mommons-bungee" project within the larger codebase<br>- It establishes "mommons-bungee" as the root project and integrates two subprojects, "mommons-worker" and "mommons-shared," by setting their respective directories<br>- This configuration facilitates modular development and shared resource management across different components of the system.</td>
			</tr>
			</table>
			<details>
				<summary><b>src</b></summary>
				<blockquote>
					<details>
						<summary><b>main</b></summary>
						<blockquote>
							<details>
								<summary><b>resources</b></summary>
								<blockquote>
									<table>
									<tr>
										<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-bungee/src/main/resources/config.yml'>config.yml</a></b></td>
										<td>- Config.yml located in mommons-bungee/src/main/resources serves as the configuration backbone for database connectivity within the project<br>- It specifies the database address, port, credentials, and connection settings, ensuring secure and reliable database interactions essential for the application's data management and operational stability across its architecture.</td>
									</tr>
									<tr>
										<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-bungee/src/main/resources/plugin.yml'>plugin.yml</a></b></td>
										<td>- MommonsProxy, defined in the plugin.yml, serves as a central loader for all dependent plugins within the Mommons architecture<br>- Positioned in the mommons-bungee/src/main/resources directory, it specifies MommonsPluginBungee as the main class, authored by itIsMaku<br>- This configuration file outlines the plugin's purpose, authorship, and version, crucial for managing dependencies and ensuring compatibility across the system.</td>
									</tr>
									</table>
								</blockquote>
							</details>
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<details>
										<summary><b>cz</b></summary>
										<blockquote>
											<details>
												<summary><b>maku</b></summary>
												<blockquote>
													<details>
														<summary><b>mommons</b></summary>
														<blockquote>
															<details>
																<summary><b>worker</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-bungee/src/main/java/cz/maku/mommons/worker/BungeeWorkerReceiver.java'>BungeeWorkerReceiver.java</a></b></td>
																		<td>- BungeeWorkerReceiver in the Mommons-Bungee project serves as a utility class for retrieving service instances managed by a Worker<br>- It facilitates access to various services by returning the specific service object associated with a given class, ensuring that components within the architecture can efficiently interact and utilize shared functionalities.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-bungee/src/main/java/cz/maku/mommons/worker/BungeeWorker.java'>BungeeWorker.java</a></b></td>
																		<td>- BungeeWorker, a subclass within the Mommons project, manages the lifecycle and integration of services within a BungeeCord plugin environment<br>- It initializes and terminates services dynamically, ensuring that each service is properly configured and logged, enhancing the plugin's operational efficiency and reliability in server management tasks.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-bungee/src/main/java/cz/maku/mommons/worker/BungeeWorkerMethod.java'>BungeeWorkerMethod.java</a></b></td>
																		<td>- BungeeWorkerMethod in the Mommons-Bungee project extends WorkerExecutable to handle BungeeCord-specific functionalities<br>- It determines whether methods are designated as commands or events by checking for the presence of BungeeCommand and BungeeEvent annotations, facilitating the dynamic execution of BungeeCord operations within the application's architecture.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-bungee/src/main/java/cz/maku/mommons/worker/WorkerBungeeServiceClass.java'>WorkerBungeeServiceClass.java</a></b></td>
																		<td>- WorkerBungeeServiceClass extends functionality within a BungeeCord plugin environment, managing asynchronous tasks and event handling<br>- It schedules SQL data retrieval and repetitive tasks, integrates custom command execution, and processes event-driven actions<br>- This class effectively bridges game server operations with backend data processes, enhancing plugin responsiveness and functionality.</td>
																	</tr>
																	</table>
																	<details>
																		<summary><b>plugin</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-bungee/src/main/java/cz/maku/mommons/worker/plugin/BungeeWorkerPlugin.java'>BungeeWorkerPlugin.java</a></b></td>
																				<td>- BungeeWorkerPlugin serves as an abstract base for plugins within the Mommons project, managing the lifecycle of BungeeWorker instances<br>- It orchestrates initialization, service registration, and cleanup processes for plugins, ensuring they properly integrate with the overarching system's architecture<br>- This setup facilitates efficient resource management and service handling during the plugin's active phases.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>annotation</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-bungee/src/main/java/cz/maku/mommons/worker/annotation/BungeeCommand.java'>BungeeCommand.java</a></b></td>
																				<td>- BungeeCommand.java defines an annotation used within the Mommons Bungee project to declare and configure new commands for a BungeeCord server environment<br>- It specifies command properties such as name, description, permissions, usage instructions, aliases, and a fallback prefix, streamlining the integration and management of commands across the system.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-bungee/src/main/java/cz/maku/mommons/worker/annotation/BungeeEvent.java'>BungeeEvent.java</a></b></td>
																				<td>- Defines an annotation, BungeeEvent, used within the Mommons Bungee project to mark methods that handle specific events in a BungeeCord environment<br>- By specifying the event class, it streamlines the integration of event-driven behaviors across the application, enhancing modularity and clarity in event management<br>- This annotation is crucial for developers to efficiently bind actions to various server events.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																</blockquote>
															</details>
															<details>
																<summary><b>bserver</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-bungee/src/main/java/cz/maku/mommons/bserver/bServerDataRepository.java'>bServerDataRepository.java</a></b></td>
																		<td>- bServerDataRepository manages a dynamic repository of server data within the Mommons project<br>- It periodically updates and synchronizes server information from a MySQL database, ensuring that the server list is current and accurate<br>- This component leverages asynchronous operations to maintain performance while handling data-intensive tasks.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-bungee/src/main/java/cz/maku/mommons/bserver/bServer.java'>bServer.java</a></b></td>
																		<td>- bServer, part of the Mommons project, manages server data interactions with a MySQL database, specifically handling server identification and operational data like online players and server type<br>- It provides asynchronous retrieval of server information, including IP and port, leveraging the project's execution service for efficient data processing.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-bungee/src/main/java/cz/maku/mommons/bserver/bLocalServerInfo.java'>bLocalServerInfo.java</a></b></td>
																		<td>- Defines a class `bLocalServerInfo` within the `mommons-bungee` project, responsible for storing and providing access to local server configuration details, specifically IP address and port number<br>- This class supports the broader architecture by encapsulating server connectivity properties, essential for network operations and inter-server communication within the system.</td>
																	</tr>
																	</table>
																</blockquote>
															</details>
															<details>
																<summary><b>plugin</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-bungee/src/main/java/cz/maku/mommons/plugin/MommonsPluginBungee.java'>MommonsPluginBungee.java</a></b></td>
																		<td>- MommonsPluginBungee serves as the core integration point for the BungeeCord platform within the Mommons project, managing configuration and database connectivity<br>- It initializes essential services, handles configuration file creation and loading, establishes MySQL connections, and ensures clean disconnection on unload, thereby setting up the infrastructure for further plugin operations and data handling.</td>
																	</tr>
																	</table>
																</blockquote>
															</details>
															<details>
																<summary><b>bungee</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-bungee/src/main/java/cz/maku/mommons/bungee/Schedulers.java'>Schedulers.java</a></b></td>
																		<td>- Schedulers.java facilitates asynchronous task management within the Mommons Bungee plugin architecture<br>- It provides methods to execute, delay, or periodically repeat tasks asynchronously using the BungeeCord proxy server's scheduling capabilities<br>- This centralizes task scheduling, enhancing maintainability and scalability by leveraging the native Bungee API.</td>
																	</tr>
																	</table>
																</blockquote>
															</details>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
				</blockquote>
			</details>
		</blockquote>
	</details>
	<details> <!-- mommons-shared Submodule -->
		<summary><b>mommons-shared</b></summary>
		<blockquote>
			<table>
			<tr>
				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/build.gradle'>build.gradle</a></b></td>
				<td>- Configures the build environment for the 'mommons-shared' module, specifying Java as the programming language and setting up Maven for publishing<br>- It defines the project's metadata, source encoding, repository sources, and manages dependencies essential for the module's functionality, including libraries for data handling, network communication, and database connectivity.</td>
			</tr>
			<tr>
				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/settings.gradle'>settings.gradle</a></b></td>
				<td>- Sets the name of the root project in the "mommons-shared" module, establishing its identity within the broader project architecture<br>- This configuration is crucial for project recognition and management across tools and scripts that automate or facilitate project operations, ensuring consistent referencing and interaction within the multi-module setup.</td>
			</tr>
			</table>
			<details>
				<summary><b>src</b></summary>
				<blockquote>
					<details>
						<summary><b>main</b></summary>
						<blockquote>
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<details>
										<summary><b>cz</b></summary>
										<blockquote>
											<details>
												<summary><b>maku</b></summary>
												<blockquote>
													<details>
														<summary><b>mommons</b></summary>
														<blockquote>
															<table>
															<tr>
																<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/Response.java'>Response.java</a></b></td>
																<td>- Response.java defines a structure for handling operation outcomes within the Mommons project, distinguishing between successful executions and exceptions<br>- It provides methods to validate responses, identify exceptions, and convert runnable tasks into standardized response formats, enhancing error handling and response consistency across the application.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/Mommons.java'>Mommons.java</a></b></td>
																<td>- Mommons.java establishes core utilities for the Mommons project, configuring a JSON parser with Gson for flexible data handling, an ExecutorService for managing asynchronous tasks, and an OkHttpClient for HTTP communications<br>- These foundational components support various functionalities across the entire codebase, enhancing data processing and network operations efficiency.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/ExceptionResponse.java'>ExceptionResponse.java</a></b></td>
																<td>- ExceptionResponse, part of the Mommons project, extends the Response class to encapsulate exception handling by including an exception object alongside standard response attributes<br>- It enhances error reporting by allowing the transmission of exception details, potentially improving debugging and logging within the system's architecture.</td>
															</tr>
															</table>
															<details>
																<summary><b>discord</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/discord/Webhook.java'>Webhook.java</a></b></td>
																		<td>- Webhook.java in the Mommons project facilitates communication with Discord services by allowing the creation and sending of customized messages via webhooks<br>- It supports text, embedded rich content, and user customization options such as username and avatar settings<br>- This component is crucial for integrating interactive and dynamic content into Discord channels.</td>
																	</tr>
																	</table>
																</blockquote>
															</details>
															<details>
																<summary><b>storage</b></summary>
																<blockquote>
																	<details>
																		<summary><b>local</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/storage/local/LocalData.java'>LocalData.java</a></b></td>
																				<td>- LocalData.java defines an interface for managing local storage operations within the Mommons project<br>- It facilitates the retrieval and updating of values stored locally, ensuring data integrity through exception handling<br>- The interface supports basic CRUD operations, enhancing the project's ability to handle data efficiently and securely across its various components.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>database</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/storage/database/Database.java'>Database.java</a></b></td>
																				<td>- Defines the essential operations for database interaction within the Mommons project, serving as a foundational interface for managing database connections<br>- It specifies methods to connect, disconnect, check connection status, and retrieve the database connection object, ensuring uniform database operations across different implementations in the codebase.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/storage/database/SQLRow.java'>SQLRow.java</a></b></td>
																				<td>- SQLRow serves as a utility class within the Mommons project, facilitating the manipulation and retrieval of data from SQL database rows<br>- It allows for storing and accessing various data types, including strings, integers, and doubles, and supports JSON object conversion using the integrated GSON library for complex data handling.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/storage/database/SQLTable.java'>SQLTable.java</a></b></td>
																				<td>- SQLTable in the Mommons project facilitates the creation and management of SQL tables within a MySQL database environment<br>- It allows dynamic definition of table structures, including column names and data types, and supports table creation with SQL commands tailored to the defined schema<br>- This class is integral for database operations across the application.</td>
																			</tr>
																			</table>
																			<details>
																				<summary><b>type</b></summary>
																				<blockquote>
																					<table>
																					<tr>
																						<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/storage/database/type/MySQL.java'>MySQL.java</a></b></td>
																						<td>- MySQL.java serves as the primary interface for managing MySQL database interactions within the Mommons project<br>- It facilitates database connectivity, executes synchronous and asynchronous queries, and checks for the existence of specific data rows<br>- This class ensures robust database operations by handling connection stability and data retrieval efficiently.</td>
																					</tr>
																					<tr>
																						<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/storage/database/type/AutoIncrement.java'>AutoIncrement.java</a></b></td>
																						<td>- Defines a class within the Mommons project that represents the auto-increment functionality typically used in database schemas<br>- Situated in the storage and database management layer of the architecture, it plays a crucial role in handling automatic index incrementation for database entries, ensuring unique identifiers for records across the system.</td>
																					</tr>
																					<tr>
																						<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/storage/database/type/Json.java'>Json.java</a></b></td>
																						<td>- Defines a Json data type within the Mommons project, specifically under the storage and database management subsystem<br>- It serves as a foundational component for handling JSON data interactions across various modules of the application, ensuring consistent data formatting and manipulation<br>- This class is integral to the project's ability to manage and store structured data efficiently.</td>
																					</tr>
																					<tr>
																						<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/storage/database/type/IdString.java'>IdString.java</a></b></td>
																						<td>- Defines a custom data type, `IdString`, within the `mommons-shared` module, specifically under the database type package<br>- It serves as a specialized identifier across the application's database interactions, enhancing data handling and consistency<br>- This class is integral to the architecture, ensuring uniform ID management throughout various components of the system.</td>
																					</tr>
																					<tr>
																						<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/storage/database/type/JDBC.java'>JDBC.java</a></b></td>
																						<td>- JDBC.java serves as a connector for Java applications to interact with databases via JDBC, managing database connections, authentication, and state<br>- It supports operations such as connecting to, disconnecting from, and checking the status of database connections, thereby facilitating robust database interactions within the broader Mommons project architecture.</td>
																					</tr>
																					<tr>
																						<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/storage/database/type/TimeStamp.java'>TimeStamp.java</a></b></td>
																						<td>- TimeStamp.java defines a custom data type within the Mommons project, specifically tailored for handling timestamp operations in the database layer<br>- Situated in the storage and database management segment of the codebase, it plays a crucial role in ensuring accurate and efficient date and time processing across various components that require time-based data handling.</td>
																					</tr>
																					<tr>
																						<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/storage/database/type/IdAutoIncrement.java'>IdAutoIncrement.java</a></b></td>
																						<td>- Defines a class within the Mommons project that likely serves as a utility for handling database operations related to automatically incrementing identifiers<br>- Positioned within the storage and database management layer of the architecture, it plays a crucial role in data integrity and auto-generation of primary keys for database entities.</td>
																					</tr>
																					</table>
																				</blockquote>
																			</details>
																		</blockquote>
																	</details>
																</blockquote>
															</details>
															<details>
																<summary><b>logger</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/logger/LoggerHandler.java'>LoggerHandler.java</a></b></td>
																		<td>- LoggerHandler, located within the mommons-shared module, enhances logging capabilities by dynamically assigning log records with thread and class-specific details<br>- It formats log entries with color-coded output for improved readability and manages the flushing and closing of system outputs, ensuring streamlined log handling across the application.</td>
																	</tr>
																	</table>
																</blockquote>
															</details>
															<details>
																<summary><b>cache</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/cache/ExpiringMap.java'>ExpiringMap.java</a></b></td>
																		<td>- ExpiringMap in the `mommons-shared` module provides a self-managing cache mechanism where entries expire after a specified duration<br>- It supports operations to add, renew, delete, and retrieve entries based on key-value pairs, automatically purging outdated items based on the predefined time unit and duration<br>- This utility enhances data handling efficiency by ensuring temporal relevance.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/cache/ExpirableValue.java'>ExpirableValue.java</a></b></td>
																		<td>- ExpirableValue.java defines a generic class for managing values that expire after a specified duration, using Java's time units<br>- It allows for setting, renewing, and automatically clearing values based on time elapsed, ensuring data freshness within the application's caching system<br>- This functionality is crucial for maintaining optimal memory usage and data integrity across the software's caching mechanisms.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/cache/ExpiringList.java'>ExpiringList.java</a></b></td>
																		<td>- ExpiringList in the mommons-shared module manages a collection of elements paired with timestamps, automatically removing items based on a predefined expiration time<br>- It supports operations such as adding, renewing, and deleting elements, ensuring the list only contains valid, non-expired items<br>- This functionality is crucial for handling time-sensitive data across the application.</td>
																	</tr>
																	</table>
																</blockquote>
															</details>
															<details>
																<summary><b>utils</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/utils/Cooldown.java'>Cooldown.java</a></b></td>
																		<td>- Cooldown.java serves as a utility within the Mommons project, managing time-based restrictions across various functionalities<br>- It establishes a cooldown period using specified time units and checks if this period has expired<br>- This mechanism is crucial for controlling the frequency of operations, enhancing performance and preventing abuse in processes that require regulated execution intervals.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/utils/Reflections.java'>Reflections.java</a></b></td>
																		<td>- Reflections.java, located within the `mommons-shared/src/main/java/cz/maku/mommons/utils` directory, provides utility functions for dynamically identifying and selecting constructors in Java classes based on given parameters<br>- This capability is essential for facilitating flexible instantiation mechanisms across the codebase, enhancing modularity and adaptability in component integration and testing scenarios.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/utils/Quadruple.java'>Quadruple.java</a></b></td>
																		<td>- Quadruple.java defines a utility class within the Mommons project, facilitating the grouping of four related values of potentially different types<br>- Positioned in the utilities package, it enhances data handling capabilities across the system by allowing complex data structures to be managed more effectively, supporting diverse functionalities throughout the application's modules.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/utils/Files.java'>Files.java</a></b></td>
																		<td>- Files.java in the mommons-shared utility package provides essential file management functions within the broader codebase<br>- It facilitates the downloading of files from specified URLs and supports recursive deletion of directories and their contents, ensuring efficient file handling and resource management across various components of the project.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/utils/Collections.java'>Collections.java</a></b></td>
																		<td>- Collections.java in the mommons-shared module provides utility functions for manipulating collections in Java<br>- It includes methods to retrieve a random element from any collection and to sort a map by its values, ensuring enhanced functionality for data handling across the application<br>- These utilities support various operations requiring element selection and order consistency in data structures.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/utils/ConsoleColors.java'>ConsoleColors.java</a></b></td>
																		<td>- Provides a comprehensive set of constants for colorizing console output, enhancing readability and debugging effectiveness across various components of the application<br>- It includes definitions for standard, bold, underlined, background, and bright color variations, facilitating diverse visual cues in terminal environments integral to the project's logging and interface functionalities.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/utils/Nets.java'>Nets.java</a></b></td>
																		<td>- Nets.java in the Mommons project provides utility functions for network-related operations<br>- It includes methods to check port availability, retrieve an available port from a list, and fetch the public IP address of the current machine using an external API<br>- These utilities support network configuration and management tasks within the application.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/utils/Rests.java'>Rests.java</a></b></td>
																		<td>- Rests.java serves as a utility class within the Mommons project, facilitating HTTP operations such as GET and POST requests<br>- It leverages the project's centralized HTTP client and JSON parser to interact with web services, handling responses by converting them into specified Java object types, optionally handling custom headers.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/utils/Pair.java'>Pair.java</a></b></td>
																		<td>- Defines a generic Pair class within the Mommons shared utility library, facilitating the storage and management of two related objects<br>- This class is essential for operations requiring paired values across various modules of the project, enhancing code reusability and maintainability by providing a standardized way to handle dual elements.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/utils/Triple.java'>Triple.java</a></b></td>
																		<td>- Triple.java defines a generic utility class within the Mommons project, facilitating the grouping of three related objects of potentially different types<br>- Positioned in the utilities package, it serves as a foundational component that enhances modularity and data handling capabilities across various modules of the system, supporting complex data structures and operations.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/utils/Timers.java'>Timers.java</a></b></td>
																		<td>- Timers.java in the Mommons shared utility library facilitates the scheduling of recurring tasks<br>- It provides a method to repeatedly execute user-defined tasks with specified initial delays and subsequent intervals<br>- This utility supports various components across the project by allowing efficient task management and timing operations, enhancing modularity and reusability within the codebase.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-shared/src/main/java/cz/maku/mommons/utils/Texts.java'>Texts.java</a></b></td>
																		<td>- Provides utility functions for text manipulation and formatting within the Mommons project, including creating structured text blocks, checking string content, formatting class names, generating progress bars, and modifying string cases<br>- These utilities support various features across the application, enhancing text handling and user interface components.</td>
																	</tr>
																	</table>
																</blockquote>
															</details>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
				</blockquote>
			</details>
		</blockquote>
	</details>
	<details> <!-- mommons-entityframework Submodule -->
		<summary><b>mommons-entityframework</b></summary>
		<blockquote>
			<table>
			<tr>
				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/test.bat'>test.bat</a></b></td>
				<td>- Executes the compiled Java archive of the Mommons Entity Framework, version 2.0.3, facilitating testing within the project's architecture<br>- By running the all-inclusive jar file, it ensures that all components of the framework are functioning as expected before deployment, enhancing reliability and stability across the system<br>- The inclusion of a pause command aids in reviewing output during manual testing.</td>
			</tr>
			<tr>
				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/build.gradle'>build.gradle</a></b></td>
				<td>- Configures the build environment for the 'mommons-entityframework' module, specifying dependencies critical for its operation, including shared libraries and worker modules<br>- It sets up the Java plugin, shadow plugin for creating fat JARs, and Maven publishing<br>- The build script also defines the main class and manages both internal and external library dependencies essential for the module's functionality.</td>
			</tr>
			<tr>
				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/settings.gradle'>settings.gradle</a></b></td>
				<td>- Defines the structure and relationships for the "mommons-entityframework" project, linking it with the "mommons-shared" and "mommons-worker" modules<br>- It sets up a multi-module Gradle project, specifying the directories for these connected modules, thereby organizing the overall build configuration and facilitating modular development across the project.</td>
			</tr>
			</table>
			<details>
				<summary><b>src</b></summary>
				<blockquote>
					<details>
						<summary><b>main</b></summary>
						<blockquote>
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<details>
										<summary><b>cz</b></summary>
										<blockquote>
											<details>
												<summary><b>maku</b></summary>
												<blockquote>
													<details>
														<summary><b>mommons</b></summary>
														<blockquote>
															<details>
																<summary><b>ef</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/Tables.java'>Tables.java</a></b></td>
																		<td>- Tables.java facilitates the dynamic creation of SQL tables based on annotated Java class definitions<br>- It interprets Java classes as database table schemas, automatically generating SQL statements to create tables with appropriate data types, default values, and primary keys, ensuring compatibility and ease of database schema management within the Mommons EntityFramework module.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/RepositoryCachingException.java'>RepositoryCachingException.java</a></b></td>
																		<td>- RepositoryCachingException, defined within the Mommons EntityFramework module, serves as a specialized exception class<br>- It is utilized across the application to handle and signal specific errors related to caching operations within repositories, enhancing error management and debugging processes in the data access layer of the architecture.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/Entities.java'>Entities.java</a></b></td>
																		<td>- Entities.java serves as a utility class within the Mommons EntityFramework, facilitating the dynamic handling of entity classes<br>- It provides methods to determine entity metadata, manage field naming conventions based on annotations, and identify primary key fields<br>- This functionality is crucial for the framework's ability to interact with and manipulate database entities effectively.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/Repositories.java'>Repositories.java</a></b></td>
																		<td>- Repositories.java facilitates the creation of repository instances for managing database operations on entities<br>- It dynamically selects an appropriate repository class based on entity annotations or defaults to a generic implementation<br>- This component supports custom ID fields and naming policies, enhancing flexibility in database interaction within the Mommons EntityFramework module.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/RepositoryCache.java'>RepositoryCache.java</a></b></td>
																		<td>- RepositoryCache in the `mommons-entityframework` module manages caching for data objects, enhancing data retrieval efficiency by storing frequently accessed items<br>- It interfaces with a repository to cache and retrieve entities using unique identifiers, and handles exceptions if caching operations fail, ensuring robust data management within the application's architecture.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/ColumnValidator.java'>ColumnValidator.java</a></b></td>
																		<td>- ColumnValidator in the Mommons EntityFramework module ensures data integrity by validating Java class fields against predefined default types and custom converters<br>- It leverages annotations to apply specific type conversions, facilitating seamless data handling and storage operations within the application's architecture<br>- This component is crucial for maintaining type safety and consistency across the system.</td>
																	</tr>
																	</table>
																	<details>
																		<summary><b>statement</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/statement/MySQLStatementImpl.java'>MySQLStatementImpl.java</a></b></td>
																				<td>- MySQLStatementImpl in the mommons-entityframework module handles the execution of SQL statements specifically tailored for MySQL databases<br>- It supports various statement types, including SELECT and table creation, managing SQL arguments dynamically<br>- The class facilitates querying and updating the database, returning results as a list of records, encapsulating database interaction logic efficiently.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/statement/CompletedStatement.java'>CompletedStatement.java</a></b></td>
																				<td>- CompletedStatement in the Mommons EntityFramework module encapsulates the result of executing a database statement, pairing the original statement with its resultant records<br>- It provides access to these records, specifically allowing retrieval of the first record, which facilitates handling of query outcomes within the broader architecture of the database interaction layer.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/statement/StatementType.java'>StatementType.java</a></b></td>
																				<td>- Defines the types of SQL statements supported within the Mommons EntityFramework module, specifically categorizing operations as SELECT, DELETE, CREATE, and UPDATE<br>- This enumeration facilitates the handling and execution of database operations, ensuring that components within the framework can uniformly interpret and process these fundamental SQL commands.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/statement/Statement.java'>Statement.java</a></b></td>
																				<td>- Statement.java defines an interface for managing SQL statements within the Mommons EntityFramework module<br>- It facilitates the creation, execution, and querying of database statements, allowing dynamic argument setting and retrieval of execution results<br>- This interface is crucial for abstracting database interactions, ensuring flexible and efficient data management across the application.</td>
																			</tr>
																			</table>
																			<details>
																				<summary><b>record</b></summary>
																				<blockquote>
																					<table>
																					<tr>
																						<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/statement/record/Record.java'>Record.java</a></b></td>
																						<td>- Record.java defines an interface crucial for data retrieval within the Mommons EntityFramework<br>- It standardizes methods to fetch and manipulate data from database records, supporting various data types and JSON conversion<br>- This interface ensures consistent data handling across different parts of the application, facilitating easier data integration and manipulation.</td>
																					</tr>
																					<tr>
																						<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/statement/record/DefaultRecordImpl.java'>DefaultRecordImpl.java</a></b></td>
																						<td>- DefaultRecordImpl serves as a fundamental component within the Mommons EntityFramework, managing data record operations<br>- It facilitates the storage and retrieval of various data types, including strings, integers, and doubles, from a centralized column map<br>- Additionally, it supports converting JSON-formatted string data into Java objects, enhancing data handling and object-relational mapping capabilities.</td>
																					</tr>
																					</table>
																				</blockquote>
																			</details>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>query</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/query/QueryBuilder.java'>QueryBuilder.java</a></b></td>
																				<td>- QueryBuilder.java defines an interface essential for constructing SQL queries within the Mommons EntityFramework<br>- It facilitates the creation of conditional statements through a default method and mandates the implementation of a method to compile the query into a string format, ensuring integration and functionality across the framework's database operations.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/query/SelectQuery.java'>SelectQuery.java</a></b></td>
																				<td>- SelectQuery.java is part of the Mommons EntityFramework, responsible for constructing SQL SELECT queries<br>- It enables dynamic specification of columns and the source table, incorporating conditions through a ConditionBuilder<br>- This class streamlines the creation of customized query strings essential for data retrieval operations within the application's database interaction layer.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/query/ConditionBuilder.java'>ConditionBuilder.java</a></b></td>
																				<td>- ConditionBuilder in the mommons-entityframework module facilitates the dynamic construction of SQL query conditions<br>- It supports methods to specify equality, containment, and logical connectors (AND, OR), assembling them into a coherent SQL WHERE clause<br>- This component is crucial for generating precise database queries based on varying runtime criteria.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>converter</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/converter/TypeConverter.java'>TypeConverter.java</a></b></td>
																				<td>- TypeConverter.java defines an interface crucial for mapping between entity fields and database columns within the Mommons EntityFramework<br>- It facilitates the conversion of data types when persisting to or fetching from a database, ensuring type safety and data integrity across the application's data access layer<br>- This interface is foundational for custom data handling strategies in the project.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>entity</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/entity/EntityClass.java'>EntityClass.java</a></b></td>
																				<td>- EntityClass.java serves as a foundational component in the Mommons EntityFramework, managing the mapping of Java class fields to database columns<br>- It dynamically resolves field names based on annotations and naming policies, ensuring compliance with SQL standards and custom attributes<br>- This class enhances data integrity and simplifies database interactions within the application architecture.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/entity/NamePolicy.java'>NamePolicy.java</a></b></td>
																				<td>- Defines an enumeration, NamePolicy, within the Mommons EntityFramework module, which specifies naming conventions for entities<br>- It offers two options: SQL, which likely adapts entity names to SQL database naming conventions, and JAVA, which aligns names with Java programming standards<br>- This enumeration aids in maintaining consistency in entity representation across different system components.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>repository</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/repository/Repository.java'>Repository.java</a></b></td>
																				<td>- Repository.java defines an interface for data access operations within the Mommons EntityFramework, supporting CRUD operations and queries on database entities<br>- It facilitates interaction with MySQL databases, manages caching, and ensures type safety through generic parameters for entity and ID types, enhancing modularity and reusability across the architecture.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/repository/DefaultRepository.java'>DefaultRepository.java</a></b></td>
																				<td>- DefaultRepository serves as a central component for managing database operations related to entity persistence in the Mommons EntityFramework<br>- It provides mechanisms for CRUD operations, leveraging SQL statements to interact with a database, and includes functionalities for querying, inserting, updating, and deleting records based on entity annotations and field validations.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>annotation</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/annotation/Id.java'>Id.java</a></b></td>
																				<td>- Defines a custom annotation `Id` used to mark fields within classes as unique identifiers in the context of the Mommons EntityFramework<br>- This annotation facilitates the automatic handling of entity identification within the framework, streamlining database operations such as retrieval and storage by clearly specifying the primary key fields in entity classes.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/annotation/AutoIncrement.java'>AutoIncrement.java</a></b></td>
																				<td>- Defines an annotation, AutoIncrement, used to mark specific fields in entity classes within the Mommons EntityFramework<br>- This annotation indicates that the marked field should automatically increment, typically used for primary key fields in database tables, facilitating unique record identification and management in the application's data layer.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/annotation/Ignored.java'>Ignored.java</a></b></td>
																				<td>- Defines an annotation within the Mommons EntityFramework module, used to mark specific fields in Java classes that should be ignored by the framework's processing mechanisms<br>- This annotation helps in customizing the behavior of data handling, ensuring that certain fields are excluded from database serialization or other automated operations.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/annotation/AttributeName.java'>AttributeName.java</a></b></td>
																				<td>- Defines a Java annotation, AttributeName, used to specify custom names for fields within entity classes in the Mommons EntityFramework module<br>- It targets field elements and retains annotation data at runtime, allowing for dynamic retrieval and usage of these custom names during database operations and entity management.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/annotation/Entity.java'>Entity.java</a></b></td>
																				<td>- Defines an annotation for marking classes as entities within the Mommons EntityFramework<br>- It allows customization of entity naming strategies and the association with specific repository classes for database operations, enhancing the framework's adaptability and integration with various data storage solutions.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/itismaku/mommons/blob/master/mommons-entityframework/src/main/java/cz/maku/mommons/ef/annotation/AttributeConvert.java'>AttributeConvert.java</a></b></td>
																				<td>- Defines a custom annotation, `AttributeConvert`, used within the Mommons EntityFramework to specify conversion logic for entity attributes<br>- It targets field elements, allowing developers to assign specific converters that handle data transformation between database representations and entity attributes, enhancing flexibility and customization in data handling across the application.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																</blockquote>
															</details>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
				</blockquote>
			</details>
		</blockquote>
	</details>
</details>

---
## 🚀 Getting Started

### ☑️ Prerequisites

Before getting started with mommons, ensure your runtime environment meets the following requirements:

- **Programming Language:** Java
- **Package Manager:** Gradle


### ⚙️ Installation

Install mommons using one of the following methods:

**Build from source:**

1. Clone the mommons repository:
```sh
❯ git clone https://github.com/itismaku/mommons
```

2. Navigate to the project directory:
```sh
❯ cd mommons
```

3. Install the project dependencies:


**Using `gradle`** &nbsp; [<img align="center" src="https://img.shields.io/badge/Gradle-02303A.svg?style={badge_style}&logo=gradle&logoColor=white" />](https://gradle.org/)

```sh
❯ gradle build
```




### 🤖 Usage
Run mommons using the following command:
**Using `gradle`** &nbsp; [<img align="center" src="https://img.shields.io/badge/Gradle-02303A.svg?style={badge_style}&logo=gradle&logoColor=white" />](https://gradle.org/)

```sh
❯ gradle run
```


### 🧪 Testing
Run the test suite using the following command:
**Using `gradle`** &nbsp; [<img align="center" src="https://img.shields.io/badge/Gradle-02303A.svg?style={badge_style}&logo=gradle&logoColor=white" />](https://gradle.org/)

```sh
❯ gradle test
```


---

## 🔰 Contributing

- **💬 [Join the Discussions](https://github.com/itismaku/mommons/discussions)**: Share your insights, provide feedback, or ask questions.
- **🐛 [Report Issues](https://github.com/itismaku/mommons/issues)**: Submit bugs found or log feature requests for the `mommons` project.
- **💡 [Submit Pull Requests](https://github.com/itismaku/mommons/blob/main/CONTRIBUTING.md)**: Review open PRs, and submit your own PRs.

<details closed>
<summary>Contributing Guidelines</summary>

1. **Fork the Repository**: Start by forking the project repository to your github account.
2. **Clone Locally**: Clone the forked repository to your local machine using a git client.
   ```sh
   git clone https://github.com/itismaku/mommons
   ```
3. **Create a New Branch**: Always work on a new branch, giving it a descriptive name.
   ```sh
   git checkout -b new-feature-x
   ```
4. **Make Your Changes**: Develop and test your changes locally.
5. **Commit Your Changes**: Commit with a clear message describing your updates.
   ```sh
   git commit -m 'Implemented new feature x.'
   ```
6. **Push to github**: Push the changes to your forked repository.
   ```sh
   git push origin new-feature-x
   ```
7. **Submit a Pull Request**: Create a PR against the original project repository. Clearly describe the changes and their motivations.
8. **Review**: Once your PR is reviewed and approved, it will be merged into the main branch. Congratulations on your contribution!
</details>

<details closed>
<summary>Contributor Graph</summary>
<br>
<p align="left">
   <a href="https://github.com{/itismaku/mommons/}graphs/contributors">
      <img src="https://contrib.rocks/image?repo=itismaku/mommons">
   </a>
</p>
</details>

---

## 🎗 License

This project is protected under the MIT License. For more details, refer to the [LICENSE](https://choosealicense.com/licenses/) file.

