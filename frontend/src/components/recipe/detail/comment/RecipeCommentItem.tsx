import React, { useState } from 'react'
import Comment from './Comment'
import CommonButton from '../../../ui/Button'
import RecipeCommentItemActions from './RecipeCommentItemActions'
import * as S from '../../../../styles/recipe/detail/comment/RecipeCommentItem.styled'
// import useIcon from '../../../../hooks/useIcon'
// import { palette } from '../../../../constants/Styles'

interface CommentItemProps {
  comment: {
    commentId: string
    profileUrl: string
    nickname: string
    createdAt: string
    message: string
    isDeleted: boolean
    replies?: Array<Comment>
    isReply?: boolean
  }
  onDelete: (commentId: string) => void
  onEdit: (commentId: string, updatedMessage: string) => void
  isReply?: boolean
}

const RecipeCommentItem: React.FC<CommentItemProps> = ({
  comment,
  onDelete,
  onEdit,
  isReply = false,
}) => {
  const [isEditing, setIsEditing] = useState(false)
  const [editedMessage, setEditedMessage] = useState(comment.message)
  const [replyMessage, setReplyMessage] = useState('')
  const [showMoreReplies, setShowMoreReplies] = useState(false)
  // const { IcCloseLight } = useIcon()
  const formattedCreatedAt = new Date(comment.createdAt)
    .toISOString()
    .split('T')[0]

  // 댓글 삭제
  const handleDeleteClick = () => {
    onDelete(comment.commentId)
  }

  // 댓글 편집
  const handleEditClick = () => {
    setIsEditing(true)
  }

  // 편집 취소
  const handleCancelClick = () => {
    setIsEditing(false)
    setEditedMessage(comment.message)
    setReplyMessage('')
  }

  // 댓글 저장
  const handleSaveClick = () => {
    onEdit(comment.commentId, editedMessage)
    setIsEditing(false)
  }

  // 대댓글 더보기 버튼
  const handleShowMoreReplies = () => {
    const remainingReplies =
      comment.replies?.filter(reply => reply.isReply) || []
    const nextVisibleReplies = remainingReplies.slice(0, 5)
    setShowMoreReplies(remainingReplies.length > nextVisibleReplies.length)
  }

  // 메시지 변경
  const handleEditedMessageChange = (
    event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    setEditedMessage(event.target.value)
  }

  // 답글 메시지 변경
  const handleReplyMessageChange = (
    event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    setReplyMessage(event.target.value)
  }

  return (
    <>
      {/* <IcCloseLight size={5} color={palette.textPrimary} /> */}
      {/* {isReply && <IcCloseLight size={5} color={palette.textPrimary} />} */}
      <S.CommentItemWrapper isReply={comment.isReply || isReply}>
        <S.CommentHeader>
          <S.CommentUserInfo>
            <S.UserImg src={comment.profileUrl} alt={`${comment.nickname}`} />
            <S.UserNickname>{comment.nickname}</S.UserNickname>
          </S.CommentUserInfo>
          <S.CommentDate>{formattedCreatedAt}</S.CommentDate>
        </S.CommentHeader>
        {isEditing ? (
          <S.CommentEdit>
            <S.CommentEditInput
              value={isEditing ? editedMessage : replyMessage}
              onChange={
                isEditing ? handleEditedMessageChange : handleReplyMessageChange
              }
            />
            <S.CommentButtonWrap>
              <CommonButton
                onClick={isEditing ? handleSaveClick : undefined}
                size="small"
                color="divider"
              >
                확인
              </CommonButton>
              <CommonButton
                onClick={handleCancelClick}
                size="small"
                color="divider"
              >
                취소
              </CommonButton>
            </S.CommentButtonWrap>
          </S.CommentEdit>
        ) : (
          <>
            {comment.isDeleted ? (
              <S.CommentContent>삭제된 댓글입니다.</S.CommentContent>
            ) : (
              <S.CommentContent>{editedMessage}</S.CommentContent>
            )}
            {!comment.isDeleted && (
              <RecipeCommentItemActions
                onEditClick={handleEditClick}
                onDeleteClick={handleDeleteClick}
                isReply={!comment.isReply}
              />
            )}
            {showMoreReplies && (
              <button type="button" onClick={handleShowMoreReplies}>
                대댓글 더보기
              </button>
            )}
          </>
        )}
      </S.CommentItemWrapper>
    </>
  )
}

RecipeCommentItem.defaultProps = {
  isReply: false,
}
export default RecipeCommentItem
