package com.springml.spark.sftp

import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.sftp.{RemoteResourceInfo, SFTPClient => JSFTPClient}
import net.schmizz.sshj.transport.verification.PromiscuousVerifier
import scala.jdk.CollectionConverters._
import java.io.File

class SFTPClient(
                  pemFile: String,
                  pemPassphrase: String,
                  username: String,
                  password: String,
                  host: String,
                  port: Int,
                  cryptoEnabled: Boolean = false,
                  cryptoKey: String = null,
                  cryptoAlgorithm: String = "AES"
                ) {

  private val ssh = new SSHClient()
  ssh.addHostKeyVerifier(new PromiscuousVerifier())

  ssh.connect(host, port)

  if (password != null && password.nonEmpty) {
    ssh.authPassword(username, password)
  } else if (pemFile != null && pemFile.nonEmpty) {
    val keyProvider = if (pemPassphrase != null && pemPassphrase.nonEmpty)
      ssh.loadKeys(pemFile, pemPassphrase)
    else
      ssh.loadKeys(pemFile)
    ssh.authPublickey(username, keyProvider)
  } else {
    throw new IllegalArgumentException("Either password or PEM key file must be provided for SFTP connection.")
  }

  private val sftp: JSFTPClient = ssh.newSFTPClient()

  def copy(source: String, target: String): String = {
    sftp.get(source, target)
    target
  }

  def copyLatest(remoteDir: String, localDir: String): String = {
    val files = sftp.ls(remoteDir).asScala.filterNot(_.getName.startsWith("."))
    if (files.isEmpty)
      throw new IllegalStateException(s"No files found in remote directory: $remoteDir")

    val latest = files.maxBy(_.getAttributes.getMtime)
    val target = new File(localDir, latest.getName).getAbsolutePath
    sftp.get(remoteDir + "/" + latest.getName, target)
    target
  }

  def copyToFTP(source: String, target: String): Unit = {
    sftp.put(source, target)
  }

  def close(): Unit = {
    sftp.close()
    ssh.disconnect()
  }
}
