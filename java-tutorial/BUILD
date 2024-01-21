load("@rules_java//java:defs.bzl", "java_binary")

package(default_visibility = ["//visibility:public"])

java_binary(
    name = "ProjectRunner",
    srcs = [
        "src/main/java/com/example/ProjectRunner.java",
        "src/main/java/com/example/EuchreHand.java",
        "src/main/java/com/example/Rank.java",
        "src/main/java/com/example/Suit.java",
    ],
    main_class = "com.example.ProjectRunner",
    deps = [":greeter"],
)

java_library(
    name = "greeter",
    srcs = ["src/main/java/com/example/Greeting.java"],
    visibility = ["//src/main/java/com/example/cmdline:__pkg__"],
)
