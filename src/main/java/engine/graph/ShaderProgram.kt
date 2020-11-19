package engine.graph

import org.joml.Matrix4f
import org.lwjgl.opengl.GL20C.*
import org.lwjgl.system.MemoryStack
import org.springframework.core.io.ClassPathResource

/**
 * @author scaventz
 * @date 12/26/2020
 */
class ShaderProgram private constructor() {
    private var programId: Int = 0
    private var vsId: Int = 0
    private var fsId: Int = 0

    /** cache of uniforms */
    private var uniformsCache: MutableMap<String, Int> = HashMap(16)

    companion object {
        @JvmStatic
        fun create(vs: String, fs: String): ShaderProgram {
            return ShaderProgram().build(vs, fs)
        }
    }

    private fun build(vs: String, fs: String): ShaderProgram {
        programId = glCreateProgram()

        val vsCode = ClassPathResource(vs).file.readBytes().decodeToString()
        val fsCode = ClassPathResource(fs).file.readBytes().decodeToString()
        vsId = createShader(vsCode, GL_VERTEX_SHADER)
        fsId = createShader(fsCode, GL_FRAGMENT_SHADER)

        glLinkProgram(programId)
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw RuntimeException("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024))
        }

        glDetachShader(programId, vsId)
        glDetachShader(programId, fsId)

        // TODO This method is used mainly for debugging purposes, and it should be removed when your game reaches production stage.
        glValidateProgram(programId)
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024))
        }
        return this
    }

    private fun getCache(uniformName: String): Int {
        var location: Int? = uniformsCache.get(uniformName)
        if (location == null) {
            location = glGetUniformLocation(programId, uniformName)
            if (location < 0) {
                throw RuntimeException("Could not find uniform:$uniformName")
            }
            // update uniformsCache
            uniformsCache.put(uniformName, location)
        }
        return location
    }

    fun setUniform(uniformName: String, value: Matrix4f) {
        // try to retrieve from cache first
        val location: Int = getCache(uniformName)
        MemoryStack.stackPush().use { stack ->
            glUniformMatrix4fv(location, false, value[stack.mallocFloat(16)])
        }
    }


    private fun createShader(shaderCode: String, shaderType: Int): Int {
        var shaderId = glCreateShader(shaderType)
        if (shaderId == 0) {
            throw RuntimeException("Error creating shader. Type: $shaderType")
        }

        glShaderSource(shaderId, shaderCode)
        glCompileShader(shaderId)

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw RuntimeException("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024))
        }
        glAttachShader(programId, shaderId)
        return shaderId;
    }

    fun getProgramId(): Int {
        return programId
    }

    fun bind() {
        glUseProgram(programId)
    }

    fun unbind() {
        glUseProgram(0)
    }

    fun cleanup() {
        unbind()
        if (programId != 0) {
            glDeleteProgram(programId)
        }
    }
}