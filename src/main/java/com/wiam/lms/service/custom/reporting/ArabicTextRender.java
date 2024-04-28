package com.wiam.lms.service.custom.reporting;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.BidiTransform;
import java.text.Bidi;
import org.xhtmlrenderer.extend.OutputDevice;
import org.xhtmlrenderer.pdf.ITextTextRenderer;
import org.xhtmlrenderer.render.JustificationInfo;

class ArabicTextRender extends ITextTextRenderer {

    @Override
    public void drawString(OutputDevice outputDevice, String string, float x, float y) {
        super.drawString(outputDevice, normalize(string), x, y);
    }

    @Override
    public void drawString(OutputDevice outputDevice, String string, float x, float y, JustificationInfo info) {
        super.drawString(outputDevice, normalize(string), x, y, info);
    }

    private String normalize(final String orig) {
        //As iText and openPdf cannot handle arabic text - and RTL languages - by default.
        //ICU4J is used to transform the text and rearrange the string arabic letters, transforming them if needed,
        //in order to show them visually inside LTR text.
        var bidiTransform = new BidiTransform();
        return bidiTransform.transform(
            orig,
            (byte) Bidi.DIRECTION_RIGHT_TO_LEFT,
            BidiTransform.Order.LOGICAL,
            (byte) Bidi.DIRECTION_RIGHT_TO_LEFT,
            BidiTransform.Order.VISUAL,
            BidiTransform.Mirroring.ON,
            ArabicShaping.LETTERS_SHAPE | ArabicShaping.LETTERS_MASK
        );
    }
}
