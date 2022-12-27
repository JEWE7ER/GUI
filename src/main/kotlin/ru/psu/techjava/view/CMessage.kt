package ru.psu.techjava.view

import tornadofx.*

class CMessage : Fragment()
{
    val text1: String by param()
    val text2: String by param()
    override val root = label(text1+text2)
}