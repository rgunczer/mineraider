package com.almagems.mineraider;

import static android.opengl.GLES20.*;


public final class FBO {

    // FBO vars
    private int m_TextureId;

    // Buffers
    private int m_ColorBuffer;
    private int m_DepthBuffer;
    private int m_FrameBuffer;

    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void create(int w, int h) {
        width = w;
        height = h;

        int[] temp = new int[1];

        // Create the color buffer
        glGenRenderbuffers(1, temp, 0);
        m_ColorBuffer = temp[0];
        glBindRenderbuffer(GL_RENDERBUFFER, m_ColorBuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_RGB, w, h);

        // Create the dept buffer
        glGenRenderbuffers(1, temp, 0); m_DepthBuffer = temp[0];
        glBindRenderbuffer(GL_RENDERBUFFER, m_DepthBuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT16, w, h);

        glGenFramebuffers(1, temp, 0); m_FrameBuffer = temp[0];
        glBindFramebuffer(GL_FRAMEBUFFER, m_FrameBuffer);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_RENDERBUFFER, m_ColorBuffer);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, m_DepthBuffer);

        // Create and Attach the texture to the FBO
        m_TextureId = Graphics.createTexture(w, h); // <-- CALL CREATE TEXTURE HERE!!!
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, m_TextureId, 0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.out.println("ERROR Creating FBO...");
            // die here!
        }

        glBindTexture(GL_TEXTURE_2D, 0);
        glBindRenderbuffer(GL_RENDERBUFFER, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, m_FrameBuffer);
    }

    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getTextureId() {
        return m_TextureId;
    }

}
