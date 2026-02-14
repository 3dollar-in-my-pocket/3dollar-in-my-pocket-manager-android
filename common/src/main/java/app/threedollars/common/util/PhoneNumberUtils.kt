package app.threedollars.common.util

object PhoneNumberUtils {

    /**
     * 한국 휴대폰번호 정규식 (010-0000-0000 형식)
     */
    private val PHONE_NUMBER_PATTERN = Regex("^010-\\d{4}-\\d{4}$")

    /**
     * 숫자만 있는 휴대폰번호 정규식 (01000000000 형식)
     */
    private val DIGITS_ONLY_PATTERN = Regex("^010\\d{8}$")

    /**
     * 휴대폰번호 유효성 검사
     * @param phoneNumber 검증할 휴대폰번호
     * @return 유효하면 true, 그렇지 않으면 false
     */
    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return PHONE_NUMBER_PATTERN.matches(phoneNumber.trim())
    }

    /**
     * 휴대폰번호를 010-0000-0000 형식으로 포맷팅
     * @param input 사용자 입력 문자열
     * @return 포맷팅된 휴대폰번호 또는 빈 문자열
     */
    fun formatPhoneNumber(input: String): String {
        // 숫자만 추출
        val digitsOnly = input.replace(Regex("[^0-9]"), "")

        return when {
            digitsOnly.isEmpty() -> ""
            digitsOnly.length <= 3 -> digitsOnly
            digitsOnly.length <= 7 -> "${digitsOnly.substring(0, 3)}-${digitsOnly.substring(3)}"
            digitsOnly.length <= 11 -> "${digitsOnly.substring(0, 3)}-${digitsOnly.substring(3, 7)}-${digitsOnly.substring(7)}"
            else -> "${digitsOnly.substring(0, 3)}-${digitsOnly.substring(3, 7)}-${digitsOnly.substring(7, 11)}"
        }
    }

    /**
     * 입력 문자열이 한국 휴대폰번호로 포맷팅 가능한지 확인
     * @param input 사용자 입력 문자열
     * @return 포맷팅 가능하면 true, 그렇지 않으면 false
     */
    fun isValidPhoneNumberInput(input: String): Boolean {
        val digitsOnly = input.replace(Regex("[^0-9]"), "")

        // Allow empty input
        if (digitsOnly.isEmpty()) return true

        // Don't allow more than 11 digits
        if (digitsOnly.length > 11) return false

        // For partial input, check progressively
        // Allow "0", "01", "010", etc.
        return when (digitsOnly.length) {
            1 -> digitsOnly.startsWith("0")
            2 -> digitsOnly.startsWith("01")
            else -> digitsOnly.startsWith("010")
        }
    }

    /**
     * 완전한 휴대폰번호 형식인지 확인
     * @param phoneNumber 확인할 휴대폰번호
     * @return 완전한 형식이면 true, 그렇지 않으면 false
     */
    fun isCompletePhoneNumber(phoneNumber: String): Boolean {
        val digitsOnly = phoneNumber.replace(Regex("[^0-9]"), "")
        return DIGITS_ONLY_PATTERN.matches(digitsOnly)
    }

    /**
     * 휴대폰번호를 API 전송을 위한 형식으로 변환 (하이픈 포함)
     * @param phoneNumber 휴대폰번호
     * @return API 전송용 포맷팅된 문자열
     */
    fun toApiFormat(phoneNumber: String): String {
        val digitsOnly = phoneNumber.replace(Regex("[^0-9]"), "")
        return if (DIGITS_ONLY_PATTERN.matches(digitsOnly)) {
            "${digitsOnly.substring(0, 3)}-${digitsOnly.substring(3, 7)}-${digitsOnly.substring(7)}"
        } else {
            ""
        }
    }
}