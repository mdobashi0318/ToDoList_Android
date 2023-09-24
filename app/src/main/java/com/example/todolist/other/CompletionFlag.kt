package com.example.todolist.other

enum class CompletionFlag(val value: String) {
    /** 未完 */
    Unfinished("0"),

    /** 完了 */
    Completion("1"),

    /** 期限切れ */
    Expired("2");


    companion object {
        fun getCompletionFlag(boolean: Boolean): CompletionFlag =
            if (boolean) Completion else Unfinished

        fun getCompletionFlag(str: String): Boolean =
            str == Completion.value
    }
}







