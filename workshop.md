# Workshop
This is the branch for the workship after the Linters part of the lecture.

## Agenda
This workshop consists of the three main parts:
1. Lint configuration;
2. Writing custom Lint rule;
3. Testing custom Lint rule.

## Part 1: Lint configuration
Lint is already there in the project as part of AGP. While discovering the project you could notice that there're some Lint rule violations already exist. We're going to enforce the policy, which prevent the number of violations from growing and also try handle some existing issues. Lets start.

- Build the application by executing the `./gradlew :app:build` command in the terminal. Observe that build is successful;
- Observe that Lint reports are produced. That's because `build` task depends on `lint` task by default:
  ![img_1.png](images/img_1.png)
- Run the Lint via `./gradlew :app:lint` command in the terminal. You should see the same report:
  ![img_1.png](images/img_2.png)
- Open the `lint-results.xml` file and investigate the issues found. There must be 17 issues;
- The build succeeds because all those issues has `warning` severity. However, for the best product quality those should be addressed;
- First of all, lets prevent the number of issues from growing. Open `app/build.gradle` file and create `lintOptions` section inside of the `android` section:
  ![img_3.png](images/img_3.png)
- Set `warningsAsErrors` property within it to `true`:
  ![img_4.png](images/img_4.png)
- Build the application by executing the `./gradlew :app:build` command in the terminal. Observe that build fails:
  ![img_5.png](images/img_5.png)
- Now lets make the build pass again;
- Lets fix the `UnusedResources`issue. There must be 5 occurrences of it in the report. Just delete the referenced resources:
  ![img_6.png](images/img_6.png)
- Fix the `IconLocation` issue. Issue description suggests moving the referenced image into `drawable-nodpi` folder. Do it:
  ![img_7.png](images/img_7.png)
  ![img_8.png](images/img_8.png)
- Fix the `Autofill` issue. We don't need auto fill, so fix it by one of the ways the rule suggests:
  ![img_9.png](images/img_9.png)
- Run the build again. Observe that report has become smaller by 7 issues:
  ![img_10.png](images/img_10.png)
- Lets imagine that we can't fix the `VectorPath` issue as nobody can provide us new resource. It means that we should suppress it;
- Open the referenced file and add `tools` namespace into it. Just start typing "tools" and you should see the quick suggestion from the Android Studio:
  ![img_11.png](images/img_11.png)
- Add `tools:ignore="VectorPath"` attribute into the root tag of the image:
  ![img_12.png](images/img_12.png)
- `Overdraw` issue might be hard to fix as it might require changing the design system. Also, in some cases the reporting of this issue is false-positive. Anyway, we don't want to **ever** fix it. Lets disable this rule;
- Open `app/build.gradle` file and navigate to the `lintOptions` section. Add there `disable 'Overdraw'` property:
  ![img_13.png](images/img_13.png)
- Run the build again. Only 7 issues blocking the successful build!
  ![img_14.png](images/img_14.png)
- There're accessibility related and backup related issues left. We can't do it right now, because we don't have necessary string resources and don't know how our product should handle backup. But we definitely want them to be addressed **in the future** and we also want new code to not violate those rules. It means that we **must not** suppress those issues or disable the related rule. We should put them into the `lint-baseline.xml` file and fix later;
- Open `app/build.gradle` file and navigate to the `lintOptions` section. Add there `baseline file("lint-baseline.xml")` property:
  ![img_15.png](images/img_15.png)
- Run the build again. Observe it fails. Read the failure reason carefully:
  ![img_16.png](images/img_16.png)
- Observe `lint-baseline.xml` file created in the `app` module. Check that it has the content similar to the `lint-result.xml`:
  ![img_17.png](images/img_17.png)
- Run the build again. Observe it passes:
  ![img_18.png](images/img_18.png)
- Commit all changes including the `lint-baseline.xml` into the repository.

## Part 2: Custom Lint rules
In our project all dimensions are set through the dimen resources. All except of the text sizes. We'll create a lint rule to warn developers about the hardcoded text size usage.
- Create new Kotlin module using "new module" wizard in Android Studio. Call module `lint-ruleset`, package `com.example.ruleset` and main class `HardcodedTextSizeUsageDetector`:
  ![img_2_1.png](images/img_2_1.png)
  ![img_2_2.png](images/img_2_2.png)
- Go to the `build.gradle` file of the new module and leave there only Kotlin plugin addition:
  ![img_2_3.png](images/img_2_3.png)
- In the same file add the dependency to the lint api. Notice, that the version must be equal to the AGP version +23:
  ![img_2_4.png](images/img_2_4.png)
- Open `HardcodedTextSizeUsageDetector` file. Make the class extend `LayoutDetector`:
  ![img_2_5.png](images/img_2_5.png)
- Create and configure issue. You can setup all fields as you wish except of the `implementation`:
  ![img_2_6.png](images/img_2_6.png)
- Make the detector visit `textSize` attributes of the view. To do that, override the `getApplicableElements` method:
  ![img_2_7.png](images/img_2_7.png)
- Implement attribute visitor by overriding `visitAttribute` method. We don't need to process `textSize` attributes not from `android` namespace and we want to report the issue whenever the value of an attribute does not reference any resource:
  ![img_2_8.png](images/img_2_8.png)
- It's time to create `IssueRegistry`. Create the class caleld `ExampleIssueRegistry`, inherit it from the `IssueRegistry` class and override properties `api` and `issues`. It is important to override `api` property, otherwise you'll get `ObsoleteLintCustomCheck` warning reported by the Lint:
  ![img_2_9.png](images/img_2_9.png)
- Add your service registry into the Java service locator. Open `lint-rules/build.gradle` file and add there the following lines:
  ![img_2_10.png](images/img_2_10.png)
- Add the custom checks to your project. Open `app/build.gradle` and add there one more dependency:
  ![img_2_11.png](images/img_2_11.png)
- Run the build. Observe it failing due to the violations of our custom rule:
  ![img_2_12.png](images/img_2_12.png)
- Open one of the files, which violates the rule and observe issue highlighting in editor:
  ![img_2_13.png](images/img_2_13.png)