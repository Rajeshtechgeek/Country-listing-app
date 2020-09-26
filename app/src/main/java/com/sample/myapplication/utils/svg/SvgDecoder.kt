package com.sample.myapplication.utils.svg

import android.util.Log
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.SimpleResource
import com.bumptech.glide.request.target.Target
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import java.io.IOException
import java.io.InputStream

/** Decodes an SVG internal representation from an [InputStream].  */
class SvgDecoder : ResourceDecoder<InputStream, SVG> {
    override fun handles(source: InputStream, options: Options): Boolean {
        return true
    }

    @Throws(IOException::class)
    override fun decode(
        source: InputStream, width: Int, height: Int, options: Options
    ): Resource<SVG>? {
        return try {
            val svg = SVG.getFromInputStream(source)
            val originalWidth = svg.documentWidth
            val originalHeight = svg.documentHeight
            //check for view box
            if (svg.documentViewBox == null) {
                // Set the viewBox attribute using the document's width and height
                svg.setDocumentViewBox(0f, 0f, svg.documentWidth, svg.documentHeight)
            }
            // Now set width and height to 100% so it will scale to fit the canvas
            svg.setDocumentWidth("100%")
            svg.setDocumentHeight("100%")
            Log.d("SvgDecoder", "ActualWidth: $originalWidth ActualHeight: $originalHeight")
            if (width != Target.SIZE_ORIGINAL) {
                svg.documentWidth = width.toFloat()
            }
            if (height != Target.SIZE_ORIGINAL) {
                svg.documentHeight = height.toFloat()
            }
            Log.d("SvgDecoder", "Width: " + svg.documentWidth + " height: " + svg.documentHeight)
            SimpleResource(svg)
        } catch (ex: SVGParseException) {
            throw IOException("Cannot load SVG from stream", ex)
        }
    }
}