.class public Lcom/tunzy/xit/DeviceSpoofer;
.super Ljava/lang/Object;

.method public static getDeviceId(Landroid/content/Context;)Ljava/lang/String;
    .registers 4
    .param p0, "context"
    :try_start_0
    invoke-virtual {p0}, Landroid/content/Context;->getContentResolver()Landroid/content/ContentResolver;
    move-result-object v1
    const-string v2, "android_id"
    invoke-static {v1, v2}, Landroid/provider/Settings$Secure;->getString(Landroid/content/ContentResolver;Ljava/lang/String;)Ljava/lang/String;
    move-result-object v0
    if-nez v0, :cond_0f
    invoke-static {}, Ljava/util/UUID;->randomUUID()Ljava/util/UUID;
    move-result-object v1
    invoke-virtual {v1}, Ljava/util/UUID;->toString()Ljava/lang/String;
    move-result-object v0
    :cond_0f
    return-object v0
    :try_end_10
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_10} :catch_11
    :catch_11
    move-exception v1
    invoke-static {}, Ljava/util/UUID;->randomUUID()Ljava/util/UUID;
    move-result-object v1
    invoke-virtual {v1}, Ljava/util/UUID;->toString()Ljava/lang/String;
    move-result-object v0
    return-object v0
.end method

.method public static spoofDevice(Landroid/content/Context;)V
    .registers 4
    invoke-static {}, Ljava/util/UUID;->randomUUID()Ljava/util/UUID;
    move-result-object v0
    invoke-virtual {v0}, Ljava/util/UUID;->toString()Ljava/lang/String;
    move-result-object v0
    const-string v1, "TunzyXitPrefs"
    const/4 v2, 0x0
    invoke-virtual {p0, v1, v2}, Landroid/content/Context;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;
    move-result-object v1
    invoke-interface {v1}, Landroid/content/SharedPreferences;->edit()Landroid/content/SharedPreferences$Editor;
    move-result-object v1
    const-string v2, "spoofed_id"
    invoke-interface {v1, v2, v0}, Landroid/content/SharedPreferences$Editor;->putString(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
    invoke-interface {v1}, Landroid/content/SharedPreferences$Editor;->apply()V
    return-void
.end method