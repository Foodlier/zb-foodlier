import React, { useState } from 'react'
import { useRecoilValue } from 'recoil'
import * as S from '../../../../styles/recipe/detail/comment/RecipeComment.styled'
import SingleComment from './SingleComment'
import ReplyComment from './ReplyComment'
import { userState } from '../../../../../recoilState'

interface Comment {
  message: string
  createdAt: string
  isDeleted: boolean
  nickname: string
  profileUrl: string
}

interface RecipeCommentProps {
  comments: Comment[]
}

function RecipeComment({ comments }: RecipeCommentProps) {
  const countState = useRecoilValue(userState)
  const [commentValue, setcommentValue] = useState('')

  const handleClick = (event: React.ChangeEvent<HTMLInputElement>) => {
    setcommentValue(event.currentTarget.value)
  }

  const onSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    try {
      const newComment: Comment = {
        message: commentValue,
        createdAt: '현재 날짜 및 시간',
        isDeleted: false,
        nickname: '현재 사용자',
        profileUrl: 'https://source.unsplash.com/random/50x50/?person',
      }

      const updatedComments = [...comments, newComment]

      setcommentValue('')

      console.log('새 댓글이 추가되었습니다:', newComment)
      console.log('업데이트된 댓글 목록:', updatedComments)
    } catch (error) {
      console.error('API 호출 오류:', error)
    }
  }

  return (
    <S.CommentContainer>
      <S.CommentTit>댓글{countState}</S.CommentTit>
      {/* Root Comment Form */}
      <S.CommentForm onSubmit={onSubmit}>
        <S.CommentInput
          type="text"
          placeholder="댓글을 입력하세요"
          onChange={handleClick}
          value={commentValue}
        />
        <S.CommentSubmit type="submit">등록</S.CommentSubmit>
      </S.CommentForm>

      {/* Comment Lists  */}
      <S.CommentList>
        <SingleComment comments={comments} />
        <ReplyComment comments={comments} />
      </S.CommentList>
      {/* {console.log(props.CommentLists)}

      {props.CommentLists &&
        props.CommentLists.map(
          (comment, index) =>
            !comment.responseTo && (
              <>
                <SingleComment
                  comment={comment}
                  postId={props.postId}
                  refreshFunction={props.refreshFunction}
                />
                <ReplyComment
                  CommentLists={props.CommentLists}
                  postId={props.postId}
                  parentCommentId={comment._id}
                  refreshFunction={props.refreshFunction}
                />
              </>
            )
        )} */}

      {/* {comments.map(comment => (
        <div key={comment.message}>
          <S.UserNickname>{comment.nickname}</S.UserNickname>
          <S.CommentContent>{comment.message}</S.CommentContent>
          <S.CommentDate>{comment.createdAt}</S.CommentDate>
          <S.UserImg src={comment.profileUrl} alt={comment.nickname} />
        </div>
      ))} */}
    </S.CommentContainer>
  )
}

export default RecipeComment
