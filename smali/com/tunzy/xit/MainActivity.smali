.class public Lcom/tunzy/xit/MainActivity;
.super Landroid/app/Activity;
.source "MainActivity.java"

.field private keyInput:Landroid/widget/EditText;
.field private activateBtn:Landroid/widget/Button;
.field private statusText:Landroid/widget/TextView;

.method public constructor <init>()V
    .registers 1
    invoke-direct {p0}, Landroid/app/Activity;-><init>()V
    return-void
.end method

.method protected onCreate(Landroid/os/Bundle;)V
    .registers 6
    invoke-super {p0, p1}, Landroid/app/Activity;->onCreate(Landroid/os/Bundle;)V
    
    const v0, 0x7f030001
    invoke-virtual {p0, v0}, Lcom/tunzy/xit/MainActivity;->setContentView(I)V
    
    const v0, 0x7f080001
    invoke-virtual {p0, v0}, Lcom/tunzy/xit/MainActivity;->findViewById(I)Landroid/view/View;
    move-result-object v0
    check-cast v0, Landroid/widget/EditText;
    iput-object v0, p0, Lcom/tunzy/xit/MainActivity;->keyInput:Landroid/widget/EditText;
    
    const v0, 0x7f080002
    invoke-virtual {p0, v0}, Lcom/tunzy/xit/MainActivity;->findViewById(I)Landroid/view/View;
    move-result-object v0
    check-cast v0, Landroid/widget/Button;
    iput-object v0, p0, Lcom/tunzy/xit/MainActivity;->activateBtn:Landroid/widget/Button;
    
    const v0, 0x7f080003
    invoke-virtual {p0, v0}, Lcom/tunzy/xit/MainActivity;->findViewById(I)Landroid/view/View;
    move-result-object v0
    check-cast v0, Landroid/widget/TextView;
    iput-object v0, p0, Lcom/tunzy/xit/MainActivity;->statusText:Landroid/widget/TextView;
    
    iget-object v0, p0, Lcom/tunzy/xit/MainActivity;->activateBtn:Landroid/widget/Button;
    new-instance v1, Lcom/tunzy/xit/MainActivity$1;
    invoke-direct {v1, p0}, Lcom/tunzy/xit/MainActivity$1;-><init>(Lcom/tunzy/xit/MainActivity;)V
    invoke-virtual {v0, v1}, Landroid/widget/Button;->setOnClickListener(Landroid/view/View$OnClickListener;)V
    
    invoke-static {p0}, Lcom/tunzy/xit/KeyManager;->getSavedKey(Landroid/content/Context;)Ljava/lang/String;
    move-result-object v0
    if-eqz v0, :goto_3f
    invoke-virtual {p0, v0}, Lcom/tunzy/xit/MainActivity;->activateKey(Ljava/lang/String;)V
    :goto_3f
    return-void
.end method

.method public activateKey(Ljava/lang/String;)V
    .registers 4
    invoke-static {p0, p1}, Lcom/tunzy/xit/KeyManager;->verifyKey(Landroid/content/Context;Ljava/lang/String;)Z
    move-result v0
    if-eqz v0, :goto_12
    iget-object v0, p0, Lcom/tunzy/xit/MainActivity;->statusText:Landroid/widget/TextView;
    const-string v1, "Key Activated!"
    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V
    new-instance v0, Landroid/content/Intent;
    const-class v1, Lcom/tunzy/xit/OverlayService;
    invoke-direct {v0, p0, v1}, Landroid/content/Intent;-><init>(Landroid/content/Context;Ljava/lang/Class;)V
    invoke-virtual {p0, v0}, Lcom/tunzy/xit/MainActivity;->startService(Landroid/content/Intent;)Landroid/content/ComponentName;
    invoke-virtual {p0}, Lcom/tunzy/xit/MainActivity;->finish()V
    :goto_12
    return-void
.end method

.class Lcom/tunzy/xit/MainActivity$1;
.super Ljava/lang/Object;
.implements Landroid/view/View$OnClickListener;

.field final synthetic this$0:Lcom/tunzy/xit/MainActivity;

.method constructor <init>(Lcom/tunzy/xit/MainActivity;)V
    .registers 2
    iput-object p1, p0, Lcom/tunzy/xit/MainActivity$1;->this$0:Lcom/tunzy/xit/MainActivity;
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V
    return-void
.end method

.method public onClick(Landroid/view/View;)V
    .registers 4
    iget-object v0, p0, Lcom/tunzy/xit/MainActivity$1;->this$0:Lcom/tunzy/xit/MainActivity;
    iget-object v0, v0, Lcom/tunzy/xit/MainActivity;->keyInput:Landroid/widget/EditText;
    invoke-virtual {v0}, Landroid/widget/EditText;->getText()Landroid/text/Editable;
    move-result-object v0
    invoke-virtual {v0}, Ljava/lang/Object;->toString()Ljava/lang/String;
    move-result-object v0
    invoke-virtual {v0}, Ljava/lang/String;->trim()Ljava/lang/String;
    move-result-object v0
    invoke-virtual {v0}, Ljava/lang/String;->length()I
    move-result v1
    if-nez v1, :cond_1c
    iget-object v0, p0, Lcom/tunzy/xit/MainActivity$1;->this$0:Lcom/tunzy/xit/MainActivity;
    iget-object v0, v0, Lcom/tunzy/xit/MainActivity;->statusText:Landroid/widget/TextView;
    const-string v1, "Enter a key"
    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V
    return-void
    :cond_1c
    iget-object v1, p0, Lcom/tunzy/xit/MainActivity$1;->this$0:Lcom/tunzy/xit/MainActivity;
    invoke-virtual {v1, v0}, Lcom/tunzy/xit/MainActivity;->activateKey(Ljava/lang/String;)V
    return-void
.end method