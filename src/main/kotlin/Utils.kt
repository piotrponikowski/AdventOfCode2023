import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

fun readLines(filename: String, test: Boolean = false): List<String> {
    return File(path(test), filename).readLines()
}

fun readText(filename: String, test: Boolean = false): String {
    return File(path(test), filename).readText()
}

fun groupLines(input: String) = input
    .split(System.lineSeparator().repeat(2))
    .map { group -> group.split(System.lineSeparator()) }

private fun path(test: Boolean) = if (test) "./src/test/resources/" else "./src/main/resources/"

fun md5Hash(str: String): String {
    val md = MessageDigest.getInstance("MD5")
    val bigInt = BigInteger(1, md.digest(str.toByteArray(Charsets.UTF_8)))
    return String.format("%032x", bigInt)
}

fun gcd(a: Long, b: Long): Long {
    if (b == 0L) return a
    return gcd(b, a % b)
}

fun lcm(a: Long, b: Long): Long {
    return a / gcd(a, b) * b
}