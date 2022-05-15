package com.example.todolist.other

enum class CompletionFlag {
    /// 未完
    Unfinished,

    /// 完了
    Completion;


    fun getCompletionString(): String =
        if (CompletionFlag.Unfinished == this) "0" else "1"


    companion object {
        fun getCompletionFlag(boolean: Boolean): CompletionFlag =
            if (boolean) Completion else Unfinished

        fun getCompletionFlag(str: String): Boolean =
            str == "1"
    }
}







