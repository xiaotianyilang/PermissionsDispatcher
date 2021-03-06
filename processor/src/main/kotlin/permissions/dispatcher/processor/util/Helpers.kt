package permissions.dispatcher.processor.util

import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.TypeName
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.processor.ELEMENT_UTILS
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.TypeMirror

fun typeMirrorOf(className: String): TypeMirror = ELEMENT_UTILS.getTypeElement(className).asType()

fun typeNameOf(it: Element): TypeName = TypeName.get(it.asType())

fun requestCodeFieldName(e: ExecutableElement) = "$GEN_REQUESTCODE_PREFIX${e.simpleString().trimDollarIfNeeded().toUpperCase()}"

fun permissionFieldName(e: ExecutableElement) = "$GEN_PERMISSION_PREFIX${e.simpleString().trimDollarIfNeeded().toUpperCase()}"

fun pendingRequestFieldName(e: ExecutableElement) = "$GEN_PENDING_PREFIX${e.simpleString().trimDollarIfNeeded().toUpperCase()}"

fun withCheckMethodName(e: ExecutableElement) = "${e.simpleString().trimDollarIfNeeded()}$GEN_WITHCHECK_SUFFIX"

fun permissionRequestTypeName(e: ExecutableElement)= "${e.simpleString().trimDollarIfNeeded().capitalize()}$GEN_PERMISSIONREQUEST_SUFFIX"

fun <A : Annotation> findMatchingMethodForNeeds(needsElement: ExecutableElement, otherElements: List<ExecutableElement>, annotationType: Class<A>): ExecutableElement? {
    val value: List<String> = needsElement.getAnnotation(NeedsPermission::class.java).permissionValue()
    return otherElements.firstOrNull {
        it.getAnnotation(annotationType).permissionValue() == value
    }
}

fun varargsParametersCodeBlock(needsElement: ExecutableElement): CodeBlock {
    val varargsCall = CodeBlock.builder()
    needsElement.parameters.forEachIndexed { i, it ->
        varargsCall.add("\$L", it.simpleString())
        if (i < needsElement.parameters.size - 1) {
            varargsCall.add(", ")
        }
    }
    return varargsCall.build()
}

fun varargsKtParametersCodeBlock(needsElement: ExecutableElement): com.squareup.kotlinpoet.CodeBlock {
    val varargsCall = com.squareup.kotlinpoet.CodeBlock.builder()
    needsElement.parameters.forEachIndexed { i, it ->
        varargsCall.add("\$L", it.simpleString())
        if (i < needsElement.parameters.size - 1) {
            varargsCall.add(", ")
        }
    }
    return varargsCall.build()
}
