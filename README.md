# Project README

## Setup Git Hooks for Code Linting on Android

### Overview

This guide outlines how to set up Git hooks to automatically run code linters and formatters before commits and pushes in your Android project. This setup uses:

-   **Spotless** for code formatting.
-   **ktlint** for Kotlin style checks.
-   **Detekt** for static code analysis.

### Folder Structure

Your project should include:

-   A `scripts` folder containing `pre-commit.sh`, `pre-push.sh`, and `git-hooks.gradle.kts`.
-   The `git-hooks.gradle.kts` script to automate copying and installing Git hooks.

### Directory Structure

    `project-root/
    │
    ├── scripts/
    │   ├── pre-commit.sh
    │   ├── pre-push.sh
    │   └── build/
    │       └── git-hooks.gradle.kts
    │
    └── build.gradle.kts`
### Git Hook Scripts

Create the following scripts in the `scripts` folder.

**File: `scripts/pre-commit.sh`**
  ```bash
    #!/bin/sh

# Function to check the current branch
check_current_branch() {
    echo "\n🚀 Checking the current git branch..."
    CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)
    if [ "$CURRENT_BRANCH" = "main" ] || [ "$CURRENT_BRANCH" = "develop" ]; then
        echo "🛑 Hold it right there! Committing directly to the '$CURRENT_BRANCH' branch? That's a big no-no!"
        echo "🚫 Direct commits to '$CURRENT_BRANCH' are like trying to use a wrench to write code—doesn't work! 😜"
        echo "\nABORTING COMMIT: You must navigate to a feature branch or create a new one to save the day! 🦸‍♂️🦸‍♀️\n"
        exit 1
    else
        echo "✅ Fantastic! You're on the '$CURRENT_BRANCH' branch, which is perfect for commits. Let's keep this awesome momentum going! 🚀✨"
    fi
}

# Function to run ktlint checks
run_ktlint_checks() {
    echo "\n🚀 Brace yourself! We're about to embark on a journey of code analysis and style checking with ktlint!"
    ./gradlew ktlintCheck --daemon > /tmp/ktlint-result
    KT_EXIT_CODE=$?

    if [ ${KT_EXIT_CODE} -ne 0 ]; then
        cat /tmp/ktlint-result
        rm /tmp/ktlint-result
        echo "\n*********************************************************************************"
        echo "     💥 Oh no! ktlint found style issues in the code! Time to fix those gremlins! 💥"
        echo "     💡 Tip: You might need your Kotlin ninja skills to resolve these issues. 🛠️"
        echo "*********************************************************************************\n"
        exit ${KT_EXIT_CODE}
    else
        rm /tmp/ktlint-result
        echo "🎉 Bravo! Your Kotlin code has passed ktlint's rigorous style checks with flying colors! Keep rocking that clean code! 🚀💫"
    fi
}

# Function to run Spotless checks
run_spotless_checks() {
    echo "\n🚀 Spotless is now analyzing and formatting your code!"
    ./gradlew spotlessApply --daemon > /tmp/spotless-result
    SPOTLESS_EXIT_CODE=$?

    if [ ${SPOTLESS_EXIT_CODE} -ne 0 ]; then
        cat /tmp/spotless-result
        rm /tmp/spotless-result
        echo "\n*********************************************************************************"
        echo "   💥 Uh-oh! Spotless found formatting issues in the code! Time to tidy up! 💥"
        echo "      💡 Tip: Check the reported issues and fix formatting errors. 🛠️"
        echo "*********************************************************************************"
        exit ${SPOTLESS_EXIT_CODE}
    else
        rm /tmp/spotless-result
        echo "🎉 Stellar job! Your code is pristine and has passed Spotless's formatting checks without a hitch! Keep shining bright! ✨🚀"
    fi
}

# Function to run Detekt checks
run_detekt_checks() {
    echo "\n🚀 Detekt is now analyzing your Kotlin code for potential issues!"
    ./gradlew detekt > /tmp/detekt-result
    DETEKT_EXIT_CODE=$?

    if [ ${DETEKT_EXIT_CODE} -ne 0 ]; then
        cat /tmp/detekt-result
        rm /tmp/detekt-result
        echo "\n*********************************************************************************"
        echo "     💥 Oh no! Detekt found issues in the code! Time to fix those issues! 💥"
        echo "     💡 Tip: Review the Detekt report to resolve these issues. 🛠️"
        echo "*********************************************************************************"
        exit ${DETEKT_EXIT_CODE}
    else
        rm /tmp/detekt-result
        echo "🎉 Fantastic work! Your Kotlin code has sailed through Detekt's analysis with ease! Onward to greatness! 🚀🌟"
    fi
}

# Function to print success message
print_success_message() {
    GIT_USERNAME=$(git config user.name)
    echo "\n *******************************************************************************"
    echo "🚀🎉 Huzzah, $GIT_USERNAME! Your code has triumphed through the Style Checker Dragon unscathed! 🐉"
    echo "Your code shines brighter than a supernova and sparkles like a constellation of stars! ✨🌌"
    echo "*******************************************************************************"
    echo "\n🚀🎉 Hold tight, $GIT_USERNAME! Your code is ready to commit and conquer new heights! 🌟✨ Keep up the amazing work! 💪\n"
}

# Main script execution
check_current_branch
run_ktlint_checks
run_spotless_checks
run_detekt_checks
print_success_message

exit 0
  ```

**File: `scripts/pre-push.sh`**
  ```bash
  #!/bin/sh

# Function to check the current branch
check_current_branch() {
    echo "\n🚀 Checking the current git branch..."
    CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)
    if [ "$CURRENT_BRANCH" = "main" ] || [ "$CURRENT_BRANCH" = "develop" ]; then
        echo "🛑 Hold it right there! Committing directly to the '$CURRENT_BRANCH' branch? That's a big no-no!"
        echo "🚫 Direct commits to '$CURRENT_BRANCH' are like trying to use a wrench to write code—doesn't work! 😜"
        echo "\nABORTING COMMIT: You must navigate to a feature branch or create a new one to save the day! 🦸‍♂️🦸‍♀️\n"
        exit 1
    else
        echo "✅ Fantastic! You're on the '$CURRENT_BRANCH' branch, which is perfect for commits. Let's keep this awesome momentum going! 🚀✨"
    fi
}

# Function to run ktlint checks
run_ktlint_checks() {
    echo "\n🚀 Brace yourself! We're about to embark on a journey of code analysis and style checking with ktlint!"
    ./gradlew ktlintCheck --daemon > /tmp/ktlint-result
    KT_EXIT_CODE=$?

    if [ ${KT_EXIT_CODE} -ne 0 ]; then
        cat /tmp/ktlint-result
        rm /tmp/ktlint-result
        echo "\n*********************************************************************************"
        echo "     💥 Oh no! ktlint found style issues in the code! Time to fix those gremlins! 💥"
        echo "     💡 Tip: You might need your Kotlin ninja skills to resolve these issues. 🛠️"
        echo "*********************************************************************************\n"
        exit ${KT_EXIT_CODE}
    else
        rm /tmp/ktlint-result
        echo "🎉 Bravo! Your Kotlin code has passed ktlint's rigorous style checks with flying colors! Keep rocking that clean code! 🚀💫"
    fi
}

# Function to run Spotless checks
run_spotless_checks() {
    echo "\n🚀 Spotless is now analyzing and formatting your code!"
    ./gradlew spotlessApply --daemon > /tmp/spotless-result
    SPOTLESS_EXIT_CODE=$?

    if [ ${SPOTLESS_EXIT_CODE} -ne 0 ]; then
        cat /tmp/spotless-result
        rm /tmp/spotless-result
        echo "\n*********************************************************************************"
        echo "   💥 Uh-oh! Spotless found formatting issues in the code! Time to tidy up! 💥"
        echo "      💡 Tip: Check the reported issues and fix formatting errors. 🛠️"
        echo "*********************************************************************************"
        exit ${SPOTLESS_EXIT_CODE}
    else
        rm /tmp/spotless-result
        echo "🎉 Stellar job! Your code is pristine and has passed Spotless's formatting checks without a hitch! Keep shining bright! ✨🚀"
    fi
}

# Function to run Detekt checks
run_detekt_checks() {
    echo "\n🚀 Detekt is now analyzing your Kotlin code for potential issues!"
    ./gradlew detekt > /tmp/detekt-result
    DETEKT_EXIT_CODE=$?

    if [ ${DETEKT_EXIT_CODE} -ne 0 ]; then
        cat /tmp/detekt-result
        rm /tmp/detekt-result
        echo "\n*********************************************************************************"
        echo "     💥 Oh no! Detekt found issues in the code! Time to fix those issues! 💥"
        echo "     💡 Tip: Review the Detekt report to resolve these issues. 🛠️"
        echo "*********************************************************************************"
        exit ${DETEKT_EXIT_CODE}
    else
        rm /tmp/detekt-result
        echo "🎉 Fantastic work! Your Kotlin code has sailed through Detekt's analysis with ease! Onward to greatness! 🚀🌟"
    fi
}

# Function to print success message
print_success_message() {
    GIT_USERNAME=$(git config user.name)
    echo "\n *******************************************************************************"
    echo "🚀🎉 Huzzah, $GIT_USERNAME! Your code has triumphed through the Style Checker Dragon unscathed! 🐉"
    echo "Your code shines brighter than a supernova and sparkles like a constellation of stars! ✨🌌"
    echo "*******************************************************************************"
    echo "\n🚀🎉 Get ready, $GIT_USERNAME! Your code is about to take flight into the repository cosmos! 🌟✨ Fasten your seatbelt for a stellar push! 🚀💫\n"
}

# Main script execution
check_current_branch
run_ktlint_checks
run_spotless_checks
run_detekt_checks
print_success_message

exit 0
  ```
### Gradle Setup

In your `scripts/build/git-hooks.gradle.kts`, add tasks which copy these scripts to the `.git/hooks` folder and make them executable.

**File: `scripts/build/git-hooks.gradle.kts`**
  ``` kotlin
    import java.util.Locale
    
    // Define a function to check if the OS is Linux or MacOS
    fun isLinuxOrMacOs(): Boolean {
        val osName = System.getProperty("os.name").lowercase(Locale.getDefault())
        return osName.contains("linux") || osName.contains("mac os") || osName.contains("macos")
    }
    
    // Define the copyGitHooks task
    tasks.register<Copy>("copyGitHooks") {
        description = "Copies the git hooks from /scripts to the .git/hooks folder."
        from("$rootDir/scripts/") {
            include("**/*.sh")
            rename { it.removeSuffix(".sh") }
        }
        into("$rootDir/.git/hooks")
        onlyIf { isLinuxOrMacOs() }
    }
    
    // Define the installGitHooks task
    tasks.register<Exec>("installGitHooks") {
        description = "Installs the pre-commit git hooks from the scripts directory."
        group = "git hooks"
        workingDir = rootDir
        commandLine("chmod", "-R", "+x", ".git/hooks/")
        dependsOn(tasks.named("copyGitHooks"))
        onlyIf { isLinuxOrMacOs() }
        doLast {
            logger.info("Git hooks installed successfully.")
        }
    }
    
    // Configure task dependencies after evaluation
    afterEvaluate {
        tasks.matching {
            it.name in listOf("preBuild", "build", "assembleDebug", "assembleRelease", "installDebug", "installRelease", "clean")
        }.configureEach {
            dependsOn(tasks.named("installGitHooks"))
        }
    }
  ```

### Main `build.gradle.kts` Configuration

In your app's module level `build.gradle.kts` file, ensure you apply the `git-hooks.gradle.kts` script.

**File: `build.gradle.kts`**

``` kotlin
// Apply the git hooks setup script
apply(from = "../scripts/build/git-hooks.gradle.kts")
```

### Installation Steps

1.  **Create the Scripts**: Create `pre-commit.sh` and `pre-push.sh` in the `scripts` directory with the contents provided above.
2.  **Configure Gradle**: Create `git-hooks.gradle.kts` in `scripts/build` with the provided content.
3.  **Update Main Build File**: Ensure the main `build.gradle.kts` applies the `git-hooks.gradle.kts` script.
4.  **Sync Project**: Sync your project with Gradle files to apply the changes.
5.  **Run the app**: This triggers on these events which install the hooks {listOf("preBuild", "build", "assembleDebug", "assembleRelease", "installDebug", "installRelease", "clean")}
6.  **Verify Hooks**: Ensure that the hooks are copied to `.git/hooks` and are executable.

### Verification

-   Attempt to commit or push changes. The hooks should automatically run the specified checks and formatters.
-   Check the output for success messages or error details.

### Notes

-   The scripts and tasks are designed to work on Linux or MacOS. Adjustments might be needed for Windows.
-   Ensure that you have `ktlint`, `Spotless`, and `Detekt` configured in your `build.gradle.kts` files.
