export default interface Comment {
  commentId: string
  message: string
  createdAt: string
  isDeleted: boolean
  nickname: string
  profileUrl: string
  referenceId: string
  isReply: boolean
  replies?: Comment[]
}
