plugins {
	alias(libs.plugins.fabric.loom.remap)
	id("maven-publish")
	alias(libs.plugins.kotlin.jvm)
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String

base {
	archivesName.set(project.property("archives_base_name") as String)
}

repositories {
	maven { url = uri("https://maven.shedaniel.me/") }
	maven { url = uri("https://maven.terraformersmc.com/releases/") }
}

loom {
	splitEnvironmentSourceSets()

	mods {
		register("yukulabtemplate") {
			sourceSet(sourceSets["main"])
			sourceSet(sourceSets["client"])
		}
	}
}

fabricApi {
	configureDataGeneration {
		client = true
	}
	configureTests {
		createSourceSet = true
		modId = "yukulabtemplate-test"
		enableGameTests = true
		enableClientGameTests = true
		eula = true
	}
}

dependencies {
	minecraft(libs.minecraft)
	mappings(loom.officialMojangMappings())
	modImplementation(libs.fabric.loader)

	// Fabric API. This is technically optional, but you probably want it anyway.
	modImplementation(libs.fabric.api)
	modImplementation(libs.fabric.language.kotlin)
	modApi(libs.cloth.config) {
		exclude(group = "net.fabricmc.fabric-api")
	}
	modImplementation(libs.modmenu)

	// Unit test
	testImplementation(libs.fabric.loader.junit)
}

tasks.test {
	useJUnitPlatform()
}

tasks.processResources {
	inputs.property("version", project.version)

	filesMatching("fabric.mod.json") {
		expand(mapOf("version" to inputs.properties["version"]))
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release.set(21)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions {
		jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
	}
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
	inputs.property("archivesName", project.base.archivesName)

	from("LICENSE") {
		rename { "${it}_${inputs.properties["archivesName"]}" }
	}
}

// configure the maven publication
publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			artifactId = project.property("archives_base_name") as String
			from(components["java"])
		}
	}

	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
	repositories {
		// Add repositories to publish to here.
		// Notice: This block does NOT have the same function as the block in the top level.
		// The repositories here will be used for publishing your artifact, not for
		// retrieving dependencies.
	}
}
