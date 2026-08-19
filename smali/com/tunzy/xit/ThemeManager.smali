.class public Lcom/tunzy/xit/ThemeManager;
.super Ljava/lang/Object;

.method public static applyTheme(Landroid/view/View;Ljava/lang/String;)V
    .registers 4
    .param p0, "view"
    .param p1, "theme"
    const/4 v0, 0x0
    invoke-virtual {p1}, Ljava/lang/String;->hashCode()I
    move-result v1
    sparse-switch v1, :sswitch_data_24
    :goto_8
    const/4 v0, -0x1
    :sswitch_9
    packed-switch v0, :pswitch_data_32
    const v0, -0xde001
    goto :goto_1f
    :pswitch_10
    const v0, -0x1f0001
    goto :goto_1f
    :pswitch_14
    const/high16 v0, -0x1000000
    goto :goto_1f
    :pswitch_17
    const v0, -0x23001
    goto :goto_1f
    :goto_1b
    const v0, -0xde001
    goto :goto_1f
    :goto_1f
    invoke-virtual {p0, v0}, Landroid/view/View;->setBackgroundColor(I)V
    return-void
    :sswitch_data_24
    .sparse-switch
        0x6c -> :sswitch_9
        0x14e -> :sswitch_9
        0x1fe -> :sswitch_9
        0x3e4a -> :sswitch_9
    .end sparse-switch
    :pswitch_data_32
    .packed-switch 0x0
        :pswitch_10
        :pswitch_14
        :pswitch_17
        :pswitch_10
    .end packed-switch
.end method